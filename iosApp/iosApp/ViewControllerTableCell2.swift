//
//  ViewControllerTableCell2.swift
//  iosApp
//
//  Created by Radoslaw Juszczyk on 08/01/2020.
//  Copyright © 2020 Radoslaw Juszczyk. All rights reserved.
//

import UIKit

class ViewControllerTableCell2: UITableViewCell {
    
    @IBOutlet weak var titleLabel: UILabel!
    
    
    func configure(text: String) {
        titleLabel.text = text
    }
}
