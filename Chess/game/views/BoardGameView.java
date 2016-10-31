/**
* Daniel Ricci <thedanny09@gmail.com>
*
* Permission is hereby granted, free of charge, to any person
* obtaining a copy of this software and associated documentation
* files (the "Software"), to deal in the Software without restriction,
* including without limitation the rights to use, copy, modify, merge,
* publish, distribute, sublicense, and/or sell copies of the Software,
* and to permit persons to whom the Software is furnished to do so, subject
* to the following conditions:
*
* The above copyright notice and this permission notice shall be included in
* all copies or substantial portions of the Software.
*
*
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
* THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
* FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
* IN THE SOFTWARE.
*/

package views;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Observable;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import controllers.BoardGameController;
import controllers.PlayerController;
import controllers.TileController;
import factories.ControllerFactory;
import models.GameModel;
import models.PlayerModel;
import models.TileModel;
import models.TileModel.NeighborPosition;

@SuppressWarnings("serial")
public final class BoardGameView extends BaseView {
	
	private final JPanel _gamePanel = new JPanel(new GridBagLayout());	
	
	public BoardGameView() {
		ControllerFactory.instance().get(PlayerController.class).populatePlayers(this);
	}

	@Override public void update(Observable obs, Object arg) {
		
		super.update(obs, arg);
		
		BoardGameController boardGameController = ControllerFactory.instance().get(BoardGameController.class);
		TileModel tileModel = (TileModel)obs;
		
		for(GameModel.Operation operation : tileModel.getOperations()) {
			switch(operation) {
			case PlayerPieceSelected:
				boardGameController.processTileSelected(tileModel);
				break;
			case PlayerPieceMoveCancel:
				boardGameController.processTileCancel(tileModel);
				break;
			case PlayerPieceMoveAccepted:
				boardGameController.processTileMove(tileModel);
				break;
			case Debugger_KingTiles:
			case Debugger_PlayerTiles:
			case Debugger_TileCoordinates:
			case EmptyTileSelected:
			case HideGuides:
			case Refresh:
			case ShowGuides:
				break;
			}
		}
	};
	
	@Override public void render() {
	
		super.render();

		Color firstColor = new Color(209, 139, 71);		
		Color secondColor = new Color(255, 205, 158);
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		
		PlayerController playerController = ControllerFactory.instance().get(PlayerController.class);
		BoardGameController boardGameController = ControllerFactory.instance().get(BoardGameController.class);

		int boardDimensions = boardGameController.getBoardDimensions();		
		Vector<Vector<TileModel>> tiles = new Vector<>();

		int colorOffset = 0;
		for (int row = 0; row < boardDimensions; ++row) {
			
			PlayerModel player = null;
			if(row == 0 || row == 1) {
				player = playerController.getPlayer(0);
			} else if(row == boardDimensions - 1 || row == boardDimensions) {
				player = playerController.getPlayer(1);
			}
			
			Vector<TileModel> tilesRow = new Vector<>();
			for (int col = 0;  col < boardDimensions; ++col) {

				// Set our grid-bad-constraints and create the game tile
				gbc.gridx = col;
				gbc.gridy = row;
				
				// Create the tile and populate its contents
				
				colorOffset = (++colorOffset % 2 == 0 ? 0 : 1);
				TileView view = new TileView(colorOffset == 0 ? firstColor : secondColor);
				TileModel tile = boardGameController.populateTile(player, row == 0 || row == boardDimensions - 1, view, this);
				
				// Set the controller of the tile and render it
				view.setController(new TileController(tile));
				view.render();
				
				// Add our components to our view
				_gamePanel.add(view, gbc);			
				tilesRow.add(tile);
			}
			
			// Ensures that the next row starts with an offset of the previous rows color
			colorOffset = (++colorOffset % 2 == 0 ? 0 : 1);

			if(!tiles.isEmpty()) {
				// Get the last row that has been rendered and link them together by 
				// reference each others top and bottom.  Once this block gets executed
				// they will be able to reference each other as neighbors
				Vector<TileModel> previous = tiles.get(tiles.size() - 1);
				for(int index = 0, oddRow = index, evenRow = index + 1; 
					index < previous.size(); 
					++index, oddRow = index - 1, evenRow = Math.min(index + 1, previous.size() - 1)) 
				{
					if(row % 2 == 0) {
						
						TileModel[] neighbors = new TileModel[] {
							previous.get(index),
							evenRow == index ? previous.get(index) : previous.get(evenRow)
						};
					
						tilesRow.get(index).setNeighbors(
							NeighborPosition.TOP,
							neighbors
						);
						
						for(TileModel neighbor : neighbors) {
							neighbor.setNeighbors(NeighborPosition.BOTTOM, tilesRow.get(index));
						}
					}
					else {
						
						TileModel[] neighbors = new TileModel[] {
							previous.get(index),
							oddRow == index ? previous.get(index) : previous.get(oddRow)
						};
						
						tilesRow.get(index).setNeighbors(
							NeighborPosition.TOP,
							neighbors
						);
						
						for(TileModel neighbor : neighbors) {
							neighbor.setNeighbors(NeighborPosition.BOTTOM, tilesRow.get(index));
						}
					}
				}
			}
			tiles.add(tilesRow);
		}
		
		add(_gamePanel);
	}


	@Override public void destroy() {
		_gamePanel.removeAll();
	}
}