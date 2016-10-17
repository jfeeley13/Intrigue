package feeley;

import java.awt.event.MouseEvent;

import junit.framework.TestCase;
import ks.client.gamefactory.GameWindow;
import ks.common.model.Card;
import ks.common.model.Column;
import ks.common.model.Deck;
import ks.common.model.Move;
import ks.common.view.ColumnView;
import ks.launcher.Main;
import ks.tests.KSTestCase;

public class TestIntrigue extends KSTestCase {
//this is the game under test.
	Intrigue game;
	
	// window for game.
	GameWindow gw;
	
	protected void setUp() {
		game = new Intrigue();
		
		// Because solitaire variations are expected to run within a container, we need to 
		// do this, even though the Container will never be made visible. Note that here
		// we select the "random seed" by which the deck will be shuffled. We use the 
		// special constant Deck.OrderBySuit (-2) which orders the deck from Ace of clubs
		// right to King of spades.
		gw = Main.generateWindow(game, Deck.OrderBySuit); 
		
		assertEquals(0, game.deck.count());
	}
	
	// clean up properly
	protected void tearDown() {
		gw.setVisible(false);
		gw.dispose();
	}
	
	
	public void testSixPileMove(){			
		//CORRECT MOVE PILE
		//take top card of queen column 1: 7 card
		Card drag = game.queens[1].get();


		//check pile[8] card is one below drag card
		assertEquals(game.sixes[8].peek().getRank(), drag.getRank()-1);

		//drag card to pile[8] 
		Move m = new MoveCard (game.queens[1], drag, game.sixes[8], "Sixes");
		
		//move was valid 
		assertTrue(m.valid(game));
		assertTrue(m.doMove(game));
		

		//check pile[8] is updated to new top card
		assertEquals(drag.getRank(),game.sixes[8].peek().getRank());
		
		//try undo
		assertTrue(m.undo(game));
		assertTrue(m.valid(game));

		//undo: drag card is not on queen pile and sixes pile is one below
		assertEquals(drag.getRank()-1 , game.sixes[8].peek().getRank());
		assertEquals(drag.getRank() , game.queens[1].peek().getRank());
		
		
		//NOT CORRECT MOVE PILE drag card to pile not correct
		
		//check pile[8] card is not one below drag card
		assertNotSame(game.sixes[3].peek().getRank(), drag.getRank()-1);
		
		//drag card to pile[8] 
		Move m1 = new MoveCard (game.queens[1], drag, game.sixes[3], "Sixes");
		
		//move was not valid 
		assertFalse(m1.valid(game));
		assertFalse(m1.doMove(game));
		

		//NOT CORRECT try drag queen
		Card dragQ = game.queens[8].get();
		assertEquals(new Card(Card.QUEEN, Card.DIAMONDS).getRank(), dragQ.getRank());

		///drag card to pile[8] 
		Move m2 = new MoveCard (game.queens[8], dragQ, game.sixes[8], "Sixes");
	
		//move was not valid 
		assertFalse(m2.valid(game));
		assertFalse(m2.doMove(game));
				
	}
	
	public void testFivePileMove(){
		Card drag = new Card(1,1);
		
		//CORRECT MOVE PILE
		//go through first column to get the KING
		for (int i=0;i<6;i++){
			drag = game.queens[1].get();
		}
		//check card is KING
		assertEquals(new Card(Card.KING, Card.DIAMONDS).getRank(), drag.getRank());
		assertTrue(game.queens[1].count() >= 1);
		assertTrue(game.fives[8].count() >= 1);


		//if drag card is KING check pile drag rank is ACE
		if (new Card(Card.KING, Card.DIAMONDS).getRank()==drag.getRank()){
			assertTrue(game.fives[8].peek().isAce());
		}
		else{
			assertEquals(game.fives[8].peek().getRank()-1, drag.getRank() );
		}

		//drag card to pile[8] 
		Move m = new MoveCard (game.queens[1], drag, game.fives[8], "Fives");
		
		//move was valid 
		assertTrue(m.valid(game));
		assertTrue(m.doMove(game));


		//check pile[8] is updated to new top card
		assertEquals(drag.getRank(), game.fives[8].peek().getRank());
		
		//try undo
		assertTrue(m.undo(game));
		assertTrue(m.valid(game));
		

		//undo: if was a king then king is back on queen col and pile is back to ace
		if (new Card(Card.KING, Card.DIAMONDS).getRank()==drag.getRank()){
			assertTrue(game.fives[8].peek().isAce());
			assertEquals(drag.getRank() , game.queens[1].peek().getRank());
		}
		else{
			assertEquals(drag.getRank()-1 , game.fives[8].peek().getRank());
			assertEquals(drag.getRank() , game.queens[1].peek().getRank());
		}
		
		//CORRECT MOVE PILE drag card to five pile not a king
		Card drag2 = new Card(1,1);
		
		//take off a few from five pile 1
		for (int i=0;i<4;i++){
			drag2 = game.fives[1].get();
		}
		
		//drag card is a 3
		assertEquals(drag2.getRank(), 3);

		//if drag card is KING check pile drag rank is ACE
		//else fives pile rank is one below drag card
		if (new Card(Card.KING, Card.DIAMONDS).getRank()==drag2.getRank()){
			assertTrue(game.fives[1].peek().isAce());
		}
		else{
			assertEquals(game.fives[1].peek().getRank()-1, drag2.getRank() );
		}
		
		//drag card to fives pile[1] 
		Move m3 = new MoveCard (game.queens[2], drag2, game.fives[1], "Fives");
		
		//move was valid 
		assertTrue(m3.valid(game));
		assertTrue(m3.doMove(game));

		//check pile[8] is updated to new top card
		assertEquals(drag2.getRank(), game.fives[1].peek().getRank());
		
		//try undo
		assertTrue(m3.undo(game));
		assertTrue(m3.valid(game));

		//undo
		assertEquals(drag2.getRank(), game.fives[1].peek().getRank()-1);

		//NOT CORRECT MOVE PILE drag card to pile not correct
		//check pile[8] card is not one below drag card
		assertNotSame(game.fives[3].peek().getRank(), drag.getRank()-1);
		
		//drag card to pile[8] 
		Move m1 = new MoveCard (game.queens[1], drag, game.fives[3], "Fives");
		
		//move was not valid 
		assertFalse(m1.valid(game));
		assertFalse(m1.doMove(game));
		

		//NOT CORRECT try drag queen
		Card dragQ = game.queens[8].get();
		assertEquals(new Card(Card.QUEEN, Card.DIAMONDS).getRank(), dragQ.getRank());

		///drag card to pile[8] 
		Move m2 = new MoveCard (game.queens[8], dragQ, game.fives[8], "Fives");
	
		//move was not valid 
		assertFalse(m2.valid(game));
		assertFalse(m2.doMove(game));
		
		assertFalse(game.hasWon());

				
	}
	
	public void testMoveCardCol() {
		
		Card drag = game.queens[2].get();
		
		assertEquals(1, game.queens[7].count());
		
		Move m = new MoveCardCol(game.queens[2], drag, game.queens[7]);
		
		System.out.println("CARDS "+ game.queens[7].count());
		//move was valid 
		assertTrue(m.valid(game));
		assertTrue(m.doMove(game));

		// undo.
		assertTrue (m.undo(game));
		assertEquals(1, game.queens[7].count());
		
	}

	
	
	public void testPileController() {

		//first create a mouse event
		MouseEvent pr = createPressed(game, game.queensView[2], 0, 0);
		game.queensView[2].getMouseManager().handleMouseEvent(pr);
		assertEquals (2, game.sixes[7].count());
		
		// drop on the first PILE
		MouseEvent rel = createReleased (game, game.sixesView[7], 0, 0);
		game.sixesView[7].getMouseManager().handleMouseEvent(rel);
		
		//System.out.println(game.sixes[7].peek());
		Move m = new MoveCard(game.queens[2], game.queens[2].get(), game.sixes[7], "Sixes");
		assertTrue(m.doMove(game));
		game.getContainer().repaint();

		assertEquals (3, game.sixes[7].count());
		
	}
	
	public void testColumnController() {

		// first create a mouse event
		MouseEvent pr = createPressed (game, game.queensView[5], 0, 0);
		game.queensView[5].getMouseManager().handleMouseEvent(pr);

		// drop on the first column
		MouseEvent rel = createReleased (game, game.sixesView[4], 0, 0);
		game.sixesView[4].getMouseManager().handleMouseEvent(rel);

		Move m = new MoveCard(game.queens[5], game.queens[5].get(), game.sixes[4], "Sixes");
		assertTrue(m.doMove(game));
		assertEquals (1, game.queens[5].count());
		game.getContainer().repaint();

		
		MouseEvent pr2 = createPressed (game, game.queensView[2], 0, 0);
		game.queensView[2].getMouseManager().handleMouseEvent(pr2);
		
		MouseEvent rel2 = createReleased (game, game.queensView[8], 0, 0);
		game.queensView[8].getMouseManager().handleMouseEvent(rel2);

		
	}
	
	/*

	public void testPlaceFromBuildablePile() {

		// first create a mouse event
		MouseEvent pr = createPressed (game, game.deckView, 0, 0);
		for (int i = 0; i < 11; i++) {
			game.deckView.getMouseManager().handleMouseEvent(pr);
		}
		
		// top card is now an ace. Click to place it home
		MouseEvent dbl = createDoubleClicked(game, game.wastePileView, 0, 0);
		game.wastePileView.getMouseManager().handleMouseEvent(dbl);
		
		assertEquals (1, game.foundation[1].count());

		// Now clear room for the 2 to go down to empty buildablePile 2
		pr = createPressed (game, game.pileViews[2], 0, game.pileViews[2].getSmallOverlap());
		ColumnView cv = game.pileViews[2].getColumnView(pr);
		MoveColumnMove mcm = new MoveColumnMove (game.piles[2], game.piles[7], (Column) cv.getModelElement(), 1); 
		mcm.doMove(game);
		
		assertEquals (1, game.piles[1].count());
		
		// move column 7 back to 1
		pr = createPressed (game, game.pileViews[7], 0, 6*game.pileViews[7].getSmallOverlap());
		cv = game.pileViews[7].getColumnView(pr);
		mcm = new MoveColumnMove (game.piles[7], game.piles[1], (Column) cv.getModelElement(), 1); 
		mcm.doMove(game);
		
		assertEquals (3, game.piles[1].count());
		
		game.getContainer().repaint();
		
		// flip that final card in 7
		FlipCardMove fcm = new FlipCardMove(game.piles[7]);
		assertTrue (fcm.valid(game));
		assertTrue (fcm.doMove(game));
		game.getContainer().repaint();
		
		// flip that final card in 2
		fcm = new FlipCardMove(game.piles[2]);
		assertTrue (fcm.valid(game));
		assertTrue (fcm.doMove(game));
		
		game.getContainer().repaint();
		
		// move 2 to 7
		// Now clear room for the 2 to go down to empty buildablePile 2
		pr = createPressed (game, game.pileViews[2], 0, 0);
		cv = game.pileViews[2].getColumnView(pr);
		if (cv == null) {
			System.out.println();
		}
		mcm = new MoveColumnMove (game.piles[2], game.piles[7], (Column) cv.getModelElement(), 1); 
		mcm.doMove(game);
		
		game.getContainer().repaint();
		
		// move 2 diamonds down from waste pile
		Card card = game.wastePile.get();
		MoveWasteToPileMove mwp = new MoveWasteToPileMove(game.wastePile, card, game.piles[2]);
		assertTrue (mwp.doMove(game));
		
		// now ready to click it to foundation.
		dbl = createDoubleClicked(game, game.pileViews[2], 0, 0);
		game.pileViews[2].getMouseManager().handleMouseEvent(dbl);
		
		assertEquals (2, game.foundation[1].count());

	}
	*/
}
