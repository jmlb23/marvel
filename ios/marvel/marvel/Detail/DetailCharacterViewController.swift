//
//  DetailCharacterViewController.swift
//  marvel
//
//  Created by jmlb23 on 6/8/21.
//

import UIKit
import Kingfisher

class DetailCharacterViewController: UIViewController {
    
    var id: Int? = nil
    
    @IBOutlet weak var backgroundThumbnail: UIImageView!
    
    @IBOutlet weak var characterName: UILabel!
    
    @IBOutlet weak var characterDescription: UILabel!
    
    var mpView = (UIApplication.shared.delegate as! AppDelegate).sharedComponent.provideCharacterView()

    
    override func viewDidLoad() {
        mpView.getDetail(id: Int32(id ?? 0), completionHandler: {succ, error in
            
        })
        mpView.start(onError: {error in
            debugPrint("ERROR \(error )")
        }, onRender: {character in
            self.characterDescription.text = character.description_?.isEmpty == true ? "Sin Descripción": character.description_
            self.characterName.text = character.name
            self.backgroundThumbnail.kf.setImage(with: URL(string: "\(character.thumbnail?.path?.replacingOccurrences(of: "http", with: "https") ?? "")/portrait_uncanny.\(character.thumbnail?.extension ?? "")"), options: KingfisherOptionsInfo([
                .scaleFactor(UIScreen.main.scale),
            ]))
        })
    }
}
