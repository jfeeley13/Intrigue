package feeley;

import ks.common.games.Solitaire;
import ks.common.model.Card;
import ks.common.model.Column;
import ks.common.model.Move;
import ks.common.model.Pile;

public class MoveCardCol extends Move {
	Column fromColumn;
	Column colTo;
	Card cardbeingdragged;
	
	public MoveCardCol(Column fromColumn, Card theCard, Column toColumn) {
		this.fromColumn = fromColumn;
		this.colTo = toColumn;
		this.cardbeingdragged = theCard;
	}

	@Override
	public boolean doMove(Solitaire theGame) {
		// Verify we can do the move.
		if (valid (theGame) == false){
			System.out.println("Move not valid");
			return false;
		}
		else{
			colTo.add(cardbeingdragged);
			System.out.println("Move valid");

			// advance score
			return true;
		}		
	}

	@Override
	public boolean undo(Solitaire game) {
		fromColumn.add(colTo.get());
		return true;
	}

	@Override
	public boolean valid(Solitaire game) {
		if(colTo.count()==1){
			return true;
		}
		else{
			return false;
		}
		
	}

}
