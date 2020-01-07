package pl.rjuszczyk.annotations

import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic

@AutoService(Processor::class)
class AnnotationProcessor : AbstractProcessor() {
    companion object {
        const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(Encapsulate::class.java.name)
    }

    override fun getSupportedSourceVersion(): SourceVersion = SourceVersion.latest()

    override fun process(annotations: MutableSet<out TypeElement>?, roundEnv: RoundEnvironment): Boolean {
        roundEnv.getElementsAnnotatedWith(Encapsulate::class.java)
            .forEach {
                if (it.kind != ElementKind.CLASS) {
                    processingEnv.messager.printMessage(Diagnostic.Kind.ERROR, "Only classes can be annotated")
                    return true
                }
                processAnnotation(it)
            }
        return false
    }

    private fun processAnnotation(element: Element) {
        val className = element.simpleName.toString()
        val pack = processingEnv.elementUtils.getPackageOf(element).toString()

        val fileName = "Encapsulated$className"
        val fileBuilder= FileSpec.builder(pack, fileName)
        val classBuilder = TypeSpec.classBuilder(fileName)

        for (enclosed in element.enclosedElements) {
            if (enclosed.kind == ElementKind.FIELD) {
                classBuilder.addProperty(
                    PropertySpec.varBuilder(enclosed.simpleName.toString(), enclosed.asType().asTypeName().asNullable(), KModifier.PRIVATE)
                        .initializer("null")
                        .build()
                )
                classBuilder.addFunction(
                    FunSpec.builder("get${enclosed.simpleName}")
                        .returns(enclosed.asType().asTypeName().asNullable())
                        .addStatement("return ${enclosed.simpleName}")
                        .build()
                )
                classBuilder.addFunction(
                    FunSpec.builder("set${enclosed.simpleName}")
                        .addParameter(ParameterSpec.builder("${enclosed.simpleName}", enclosed.asType().asTypeName().asNullable()).build())
                        .addStatement("this.${enclosed.simpleName} = ${enclosed.simpleName}")
                        .build()
                )
            }
        }
        val file = fileBuilder.addType(classBuilder.build()).build()
        val kaptKotlinGeneratedDir = processingEnv.options[KAPT_KOTLIN_GENERATED_OPTION_NAME]
        file.writeTo(File(kaptKotlinGeneratedDir))
    }
}