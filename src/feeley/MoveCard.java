package feeley;

import ks.common.games.Solitaire;
import ks.common.model.Card;
import ks.common.model.Column;
import ks.common.model.Move;
import ks.common.model.Pile;

public class MoveCard extends Move{
	Pile pileto;
	Column queens;
	Card cardbeingdragged;
	String kind;


	public MoveCard(Column from,Card cardBeingDragged, Pile to, String pilekind){
		this.pileto = to;
		this.queens = from;
		this.cardbeingdragged = cardBeingDragged;
		kind = pilekind;
	}

	@Override
	public boolean doMove(Solitaire theGame) {

		// Verify we can do the move.
		if (valid (theGame) == false){
			System.out.println("Move not valid");
			return false;
		}
		else{
			pileto.add(cardbeingdragged);
			System.out.println("Move valid");

			// advance score
			theGame.updateScore (+1);
			return true;
		}
	}

	@Override
	public boolean undo(Solitaire theGame) {
		queens.add(pileto.get());
		// decrease score
		theGame.updateScore (-1);
		return true;
	}

	@Override
	public boolean valid(Solitaire game) {

		// VALIDATION:
		boolean validation = true;

		// If draggingCard is null, then no action has yet taken place.
		if (cardbeingdragged == null) {
			System.out.println("Card is null");
		
			/*	if(kind == "Fives"){
				// if the queen column & fives pile isnt empty, card rank is 1 below pile rank
				if (queens.count() > 0 && pileto.count() > 0 && (queens.rank() == pileto.rank() - 1) ){
					validation = true;
				} else {
					validation = false;
				}
	
				// if the queen column & fives pile isnt empty, pile is at ACES then can build to king
				if (queens.count() > 0 && pileto.count() > 0  && queens.rank()  == Card.KING && pileto.rank() == 1) {
					validation = true;
				} else {
					validation = false;
				}
			}
			else{
				// if the queen column & sixes pile isnt empty, card rank is 1 above pile rank
				if (queens.count() > 0 && pileto.count() > 0 && (queens.rank() == pileto.rank() + 1) ){
					validation = true;
				} else {
					validation = false;
				}
			}
			*/
		} else {
			if(kind == "Fives"){
				// the action must have taken place, so act on the card.
				// if the queen column & fives pile isnt empty, card rank is 1 below pile rank
				if (queens.count() >= 1 && pileto.count() >= 1 && (cardbeingdragged.getRank() == pileto.rank() - 1) ){
					validation = true;
					//System.out.println("card rank of 1 below");
				} 
				// if the queen column & fives pile isnt empty, pile is at ACES then can build to king
				else if (queens.count() > 0 && pileto.count() > 0  && cardbeingdragged.getRank()  == Card.KING && pileto.rank() == 1) {
					validation = true;
					//System.out.println("card rank goes from aces to king");
				} 
				else {
					validation = false;
					System.out.println("valid "+ validation);
				}
			}
			else {//sixes pile
				// the action must have taken place, so act on the card.
				// if the queen column & sixes pile isnt empty, card rank is 1 above pile rank
				if (queens.count() >= 1 && pileto.count() >= 1 && (cardbeingdragged.getRank() == pileto.rank() + 1) ){
					validation = true;
				} 
				else {
					validation = false;
					System.out.println("valid "+ validation);
				}
			}
		}

		return validation;
	}



}
