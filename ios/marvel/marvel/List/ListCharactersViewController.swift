//
//  ViewController.swift
//  marvel
//
//  Created by jmlb23 on 5/8/21.
//

import UIKit
import presentation
import Kingfisher

class ListCharactersViewController: UIViewController {
    
    var characters: [DomainCharacter] = []

    @IBOutlet weak var tableCharacters: UITableView!
    
    var mpView = (UIApplication.shared.delegate as! AppDelegate).sharedComponent.provideCharacterListView()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        tableCharacters.dataSource = self
        mpView.getList(page: 1, completionHandler: { error,arg  in
            debugPrint(arg)
        })
        mpView.start(onError: {error in
            debugPrint("ERROR \(error )")
        }, onRender: {characters in
            self.characters = characters
            self.tableCharacters.reloadData()
        })
        
        
    }

    override func viewDidDisappear(_ animated: Bool) {
        mpView.dispose()
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if
            let detail = segue.destination as? DetailCharacterViewController,
            let sender = (sender as? ListCharacterCell)
        {
            detail.id = sender.id
        }
    }
}

extension ListCharactersViewController : UITableViewDataSource{
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        characters.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "CharacterCell" , for: indexPath) as! ListCharacterCell
        let character = characters[indexPath.item]
        cell.id = character.id?.intValue
        cell.characterDescription.text = character.description_?.isEmpty == true ? "Sin Descripción": character.description_
        cell.characterName.text = character.name
        cell.characterThumbnail.kf.setImage(with: URL(string: "\(character.thumbnail?.path?.replacingOccurrences(of: "http", with: "https") ?? "")/portrait_xlarge.\(character.thumbnail?.extension ?? "")"), options: KingfisherOptionsInfo([
            .scaleFactor(UIScreen.main.scale),
        ]))
        return cell
    }
}
