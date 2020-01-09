//
//  ViewController.swift
//  iosApp
//
//  Created by Radoslaw Juszczyk on 02/01/2020.
//  Copyright © 2020 Radoslaw Juszczyk. All rights reserved.
//

import UIKit
import shared
import Firebase

class MyDataSource: NSObject, UITableViewDataSource {
    
    var items: [String] = []
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return items.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {

        let cell = tableView.dequeueReusableCell(withIdentifier: "cell") as! ViewControllerTableCell
        cell.configure(text: items[indexPath.row])
        return cell
    }
}

class MyDataSource2: NSObject, UITableViewDataSource {
    
    var items: [String] = []
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return items.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {

        let cell = tableView.dequeueReusableCell(withIdentifier: "cell") as! ViewControllerTableCell2
        cell.configure(text: items[indexPath.row])
        return cell
    }
}


class ViewController: UIViewController {
    
    var mainViewModel : MainViewModel!
    var firebaseTestViewModel : FirebaseTestViewModel!
    
    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var tableView2: UITableView!
    @IBOutlet weak var editText: UITextField!
    @IBOutlet weak var sendButton: UIButton!
    
    let dataSource1 = MyDataSource()
    let dataSource2 = MyDataSource2()
    
    @IBAction func onSendClicked(_ sender: Any) {
        firebaseTestViewModel.send(item: editText.text!)
        editText.text = ""
    }
    
    @IBAction func onTextChanged(_ sender: Any) {
        if(editText.text == nil || editText.text!.isEmpty) {
            sendButton.isEnabled = false
        } else {
            sendButton.isEnabled = true
        }
    }
    override func viewDidLoad() {
        super.viewDidLoad()
        FirebaseApp.configure()
        tableView.dataSource = dataSource1
        tableView2.dataSource = dataSource2
        
        let todosApi = TodosApi()
        let todosDatabase = TodosDatabase(todoModelQueries: DatabaseKt.createDatabase().todoModelQueries)
        let nativeMainViewModel = NativeMainViewModel(todosRepository: TodosRepository(todosApi: todosApi, todosDatabase: todosDatabase))
        
        mainViewModel = MainViewModel(nativeMainViewModel: nativeMainViewModel)
        mainViewModel.observeState { state in
            switch state {
            case is NativeMainViewModel.Loading:
                let loading = state as! NativeMainViewModel.Loading
                if(loading.cachedData != nil) {
                    self.helloText?.text = "LOADING (cached = \(loading.cachedData!.count))"
                } else {
                    //self.helloText.text = "LOADING"
                }
                self.retry?.isHidden = true
                
            case is NativeMainViewModel.Loaded:
                let loaded = state as! NativeMainViewModel.Loaded
                self.helloText?.text = "LOADED = \(loaded.data.count)"
                self.dataSource1.items = loaded.data.map { $0.title }
                self.tableView.reloadData()
                self.retry?.isHidden = true
                
            case is NativeMainViewModel.Failed:
                let failed = state as! NativeMainViewModel.Failed
                if(failed.cachedData != nil) {
                    self.helloText?.text = "FAILED = \(failed.errorMessage)\n (cached = \(failed.cachedData!.count))"
                } else {
                    self.helloText?.text = "FAILED = \(failed.errorMessage)"
                }
                self.retry?.isHidden = false
                
            default:fatalError("should never happen")
            }
        }
        
        firebaseTestViewModel = FirebaseTestViewModel(nativeFirebaseTestViewModel: NativeFirebaseTestViewModel(firebaseTestRepository: FirebaseTestRepository()))
        firebaseTestViewModel.observeState { state in
            switch state {
            case is NativeFirebaseTestViewModel.Loaded:
                let loaded = state as! NativeFirebaseTestViewModel.Loaded
            
                self.dataSource2.items = loaded.items
                
                self.tableView2.reloadData()
                
            default: break
            }
        }
    }
    
    deinit {
        mainViewModel.cancel()
        firebaseTestViewModel.cancel()
    }
    
    @IBOutlet weak var retry: UIButton?
    @IBOutlet weak var helloText: UILabel?
    
    //    @IBAction func onInsertClicked(_ sender: Any) {
    //        mainViewModel.retry()
    //    }
}

enum MyError: Error {
    case runtimeError(String)
}

