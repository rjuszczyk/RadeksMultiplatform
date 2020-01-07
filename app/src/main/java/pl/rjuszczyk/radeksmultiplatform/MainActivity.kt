package pl.rjuszczyk.radeksmultiplatform

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import pl.rjuszczyk.radeksmultiplatform.viewmodel.MainViewModel
import pl.rjuszczyk.radeksmultiplatform.viewmodel.NativeMainViewModel

@Suppress("EXPERIMENTAL_API_USAGE")
class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel
    private val todosAdapter = TodosAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        todosList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        todosList.adapter = todosAdapter

        mainViewModel = ViewModelProviders.of(this, MainViewModelFactory()).get(MainViewModel::class.java)
        mainViewModel.getStateLiveData().observe(this, Observer { state ->
            when(state) {
                is NativeMainViewModel.Loading -> {
                    progress.visibility = View.VISIBLE
                    retry.visibility = View.GONE
                    errorMessage.visibility = View.GONE

                    state.cachedData?.let {
                        test.text = "Test (${it.size})"
                        todosAdapter.setData(it)
                        cachedDataInfo.visibility = View.VISIBLE
                        todosList.visibility = View.VISIBLE
                    }?:run {
                        cachedDataInfo.visibility = View.GONE
                        todosList.visibility = View.GONE

                        test.text = "Test (null)"
                    }
                }
                is NativeMainViewModel.Loaded -> {
                    progress.visibility = View.GONE
                    retry.visibility = View.GONE
                    errorMessage.visibility = View.GONE

                    todosAdapter.setData(state.data)
                    cachedDataInfo.visibility = View.GONE
                    todosList.visibility = View.VISIBLE

                    test.text = "Test (${state.data.size})"
                }
                is NativeMainViewModel.Failed -> {
                    progress.visibility = View.GONE
                    retry.visibility = View.GONE
                    errorMessage.visibility = View.VISIBLE
                    errorMessage.text = state.errorMessage

                    state.cachedData?.let {
                        test.text = "Test (${it.size})"
                        todosAdapter.setData(it)
                        cachedDataInfo.visibility = View.VISIBLE
                        todosList.visibility = View.VISIBLE
                    }?:run {
                        test.text = "Test (null)"
                        cachedDataInfo.visibility = View.GONE
                        todosList.visibility = View.GONE
                    }
                }
            }
        })

        retry.setOnClickListener {
            mainViewModel.retry()
        }
        test.setOnClickListener {
            mainViewModel.retry()
        }
    }
}
