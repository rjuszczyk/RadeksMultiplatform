package pl.rjuszczyk.radeksmultiplatform

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import pl.rjuszczyk.radeksmultiplatform.viewmodel.MainViewModel
import pl.rjuszczyk.radeksmultiplatform.viewmodel.NativeFirebaseTestViewModel
import pl.rjuszczyk.radeksmultiplatform.viewmodel.NativeMainViewModel
import test.pl.rjuszczyk.radeksmultiplatform.viewmodel.FirebaseTestViewModel

@Suppress("EXPERIMENTAL_API_USAGE")
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initMainViewModel()
        initFirebaseTestViewModel()
    }

    /* ===================== MainViewModel =================*/
    /*
        Observers SQL Delight database
        Performs network request with ktor-client
        Deserializes JSON response with kotlin-serializable
     */
    private lateinit var mainViewModel: MainViewModel
    private val todosAdapter = SimpleStringAdapter()
    fun initMainViewModel() {
        todosList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        todosList.adapter = todosAdapter

        mainViewModel = ViewModelProviders.of(this,
            MainViewModelFactory()
        ).get(MainViewModel::class.java)
        mainViewModel.getStateLiveData().observe(this, Observer { state ->
            when(state) {
                is NativeMainViewModel.Loading -> {
                    progress.visibility = View.VISIBLE
                    retry.visibility = View.GONE
                    errorMessage.visibility = View.GONE

                    state.cachedData?.let {
                        test.text = "Test (${it.size})"
                        todosAdapter.setData(it.map { it.title })
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

                    todosAdapter.setData(state.data.map { it.title })
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
                        todosAdapter.setData(it.map { it.title })
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



    /* ===================== FirebaseTestViewModel =================*/
    /*
        observes data from firebase firestore
        sends data to firebase firestore
     */
    private lateinit var firebaseTestViewModel: FirebaseTestViewModel
    private val firestoreItemsAdapter = SimpleStringAdapter()
    fun initFirebaseTestViewModel() {
        firestoreList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        firestoreList.adapter = firestoreItemsAdapter

        firebaseTestViewModel = ViewModelProviders.of(this,
            FirebaseTestViewModelFactory()
        ).get(FirebaseTestViewModel::class.java)

        firebaseTestViewModel.stateLiveData.observe(this, Observer { state ->
            when(state) {
                is NativeFirebaseTestViewModel.Loading -> {
                    firebaseProgress.visibility = View.VISIBLE
                    firestoreList.visibility = View.GONE
                }
                is NativeFirebaseTestViewModel.Loaded -> {
                    firestoreList.visibility = View.VISIBLE
                    firebaseProgress.visibility = View.GONE
                    firestoreItemsAdapter.setData(state.items)
                }
            }
        })

        firestoreMessage.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(p0: Editable) {

            }

            override fun beforeTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {
                firestoreSend.isEnabled = !p0.isEmpty()
            }

        })
        firestoreSend.setOnClickListener {
            firebaseTestViewModel.send(firestoreMessage.text.toString())
            firestoreMessage.setText("")
        }
    }
}
