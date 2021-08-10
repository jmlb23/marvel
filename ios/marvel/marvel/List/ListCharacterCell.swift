//
//  ListCharacterCell.swift
//  marvel
//
//  Created by jmlb23 on 5/8/21.
//

import Foundation
import UIKit

class ListCharacterCell : UITableViewCell {
    var id: Int? = nil
    @IBOutlet weak var characterName: UILabel!
    @IBOutlet weak var characterDescription: UILabel!
    @IBOutlet weak var characterThumbnail: UIImageView!
}
