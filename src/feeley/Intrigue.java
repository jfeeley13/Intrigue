package feeley;

import heineman.klondike.FoundationController;

import java.awt.Dimension;

import ks.common.controller.SolitaireMouseMotionAdapter;
import ks.common.controller.SolitaireReleasedAdapter;
import ks.common.games.Solitaire;
import ks.common.games.SolitaireUndoAdapter;
import ks.common.model.Card;
import ks.common.model.Column;
import ks.common.model.Deck;
import ks.common.model.MultiDeck;
import ks.common.model.Pile;
import ks.common.view.CardImages;
import ks.common.view.ColumnView;
import ks.common.view.DeckView;
import ks.common.view.IntegerView;
import ks.common.view.PileView;
import ks.launcher.Main;

public class Intrigue extends Solitaire {
	MultiDeck deck;
	Column queens[] = new Column [9];
	Pile fives[] = new Pile [9];
	Pile sixes[] = new Pile [9];
	DeckView deckView;
	PileView fivesView[] = new PileView [9];
	PileView sixesView[] = new PileView [9];
	ColumnView queensView[] = new ColumnView [9];
	IntegerView scoreView;
	IntegerView	numLeftView;
	
	public Dimension getPreferredSize() {
		  return new Dimension (1000, 1000);
		}

	/**
	 * 
	 */
	@Override
	public String getName() {
		return "Jefeeley-Intrigue";
	}

	@Override
	public boolean hasWon() {
		int emp = 0;
		boolean won = false;

		for (int i=1; i<=8;i++){
			if (fives[i].count() == 6 && sixes[i].count() == 6){
				emp++;
			}
		}
		
		if (emp == 8){
				won=true;
		}
		else {
			won=false;
		}
		return won;
	}

	@Override
	public void initialize() {
		int qcolNum = 1, fcolNum = 1, scolNum = 1;
		int expectNumf=1, expectNums=1;
		int expecting[] = new int[9];
		int expectingf[] = new int[9];
		boolean placed=false;
		Card putback[] = new Card[100];
		int cards=0;
		
		// initialize model
		initializeModel(getSeed());
		initializeView();
		initializeControllers();
		
		Card cqueen = new Card(12, 1);
		Card cfive = new Card(5,1);
		Card csix = new Card(6,1);
		for(int i=0;i<deck.count();i++){			//go through deck and find first queen
			Card card = deck.get();
			if (card.sameRank(cqueen)){
				queens[1].add(card); 				//start with queen
				break;
			}
			else{									//put back all the cards that weren't the queen
				putback[i]=card;
				cards++;
			}
		}
		for(int i=0;i<cards;i++){
			deck.add(putback[i]);
		}
		
		while (deck.count() > 0){ 					//deal until all cards are dealt
			placed=false;
			Card card = deck.get();
			if (card.sameRank(cqueen)){				//every queen starts new column
				qcolNum++;
				queens[qcolNum].add(card);
			}
			else if (card.sameRank(cfive)){			//every 5 starts new pile
				fives[fcolNum].add(card);
				placed = true;
				expectNumf = (card.getRank() - 1);
				expectingf[fcolNum]=expectNumf;
				fcolNum++;
			}
			else if (card.sameRank(csix)){			//if card is a six
				sixes[scolNum].add(card);			//add to new column
				placed = true;
				expectNums = (card.getRank() + 1);	//next num expecting is ++
				expecting[scolNum]=expectNums;
				scolNum++;
			}
			else{	//place cards in piles 
				if(card.getRank() <5  || card.getRank() == card.KING){ //if below a five or king goes in the 5 pile
					for (int i=1;i<=8;i++){
						if (expectingf[i] == card.getRank()){
							fives[i].add(card);
							if (card.getRank() == card.ACE){
								expectingf[i] = card.KING;	//if ace next build down to king
							}
							else expectingf[i] = card.getRank()-1;	//update next expecting number
							placed = true;
							break;
						}
					}
				}
				if(card.getRank() > 6){	//if above a 6 check the sixes pile

					for (int i=1;i<=8;i++){
						if (expecting[i] == card.getRank()){
							sixes[i].add(card);
							expecting[i] = card.getRank()+1;	//update next expecting number
							placed = true;
							//System.out.println(card.getRank()+" placed on pile " + i );
							break;
						}
					}
				}
				if (placed == false){
					queens[qcolNum].add(card);
				}
			}
		}
		
		updateNumberCardsLeft (deck.count());		
	}

	private void initializeControllers() {

		// Now for each Pile.
		for (int i = 1; i <= 8; i++) {
			fivesView[i].setMouseAdapter (new PileController (this, fivesView[i], "Fives"));
			fivesView[i].setMouseMotionAdapter (new SolitaireMouseMotionAdapter (this));
			fivesView[i].setUndoAdapter (new SolitaireUndoAdapter(this));
		}
		for (int i = 1; i <= 8; i++) {
			sixesView[i].setMouseAdapter (new PileController (this, sixesView[i], "Sixes"));
			sixesView[i].setMouseMotionAdapter (new SolitaireMouseMotionAdapter (this));
			sixesView[i].setUndoAdapter (new SolitaireUndoAdapter(this));
		}
		
		// Now for each Foundation.
		for (int i = 1; i <= 8; i++) {
			queensView[i].setMouseAdapter (new ColumnController (this, queensView[i]));
			queensView[i].setMouseMotionAdapter (new SolitaireMouseMotionAdapter (this));
			queensView[i].setUndoAdapter (new SolitaireUndoAdapter(this));
		}

		// Ensure that any releases (and movement) are handled by the non-interactive widgets
		numLeftView.setMouseMotionAdapter (new SolitaireMouseMotionAdapter(this));
		numLeftView.setMouseAdapter (new SolitaireReleasedAdapter(this));
		numLeftView.setUndoAdapter (new SolitaireUndoAdapter(this));

		// same for scoreView
		scoreView.setMouseMotionAdapter (new SolitaireMouseMotionAdapter(this));
		scoreView.setMouseAdapter (new SolitaireReleasedAdapter(this));
		scoreView.setUndoAdapter (new SolitaireUndoAdapter(this));

		// Finally, cover the Container for any events not handled by a widget:
		getContainer().setMouseMotionAdapter(new SolitaireMouseMotionAdapter(this));
		getContainer().setMouseAdapter (new SolitaireReleasedAdapter(this));
		getContainer().setUndoAdapter (new SolitaireUndoAdapter(this));

		
	}

	private void initializeView() {
		CardImages ci = getCardImages();

		deckView = new DeckView (deck);
		deckView.setBounds (20,140, ci.getWidth(), ci.getHeight());
		container.addWidget (deckView);

		// create PileViews, one after the other (default to 13 full cards -- more than we'll need)
		for (int pileNum = 1; pileNum <=8; pileNum++) {
			fivesView[pileNum] = new PileView (fives[pileNum]);
			fivesView[pileNum].setBounds ((100+(pileNum*10)) +(ci.getWidth()*pileNum), 40, ci.getWidth(), ci.getHeight());
			container.addWidget (fivesView[pileNum]);
		}

		// create PileViews, one after the other.
		for (int pileNum = 1; pileNum <=8; pileNum++) {
			sixesView[pileNum] = new PileView (sixes[pileNum]);
			sixesView[pileNum].setBounds ((100+(pileNum*10)) +(ci.getWidth()*pileNum), 50+ci.getHeight(), ci.getWidth(), ci.getHeight());
			container.addWidget (sixesView[pileNum]);
		}
		
		for (int colNum = 1; colNum <=8; colNum++) {
			queensView[colNum] = new ColumnView (queens[colNum]);
			queensView[colNum].setBounds ((100+(colNum*10)) +(ci.getWidth()*colNum), 60+(2*ci.getHeight()), ci.getWidth(), 13*ci.getHeight());
			container.addWidget (queensView[colNum]);
		}

		scoreView = new IntegerView (getScore());
		scoreView.setFontSize (14);
		scoreView.setBounds (170+(7*ci.getWidth()), 0, 60, 40);
		container.addWidget (scoreView);

		numLeftView = new IntegerView (getNumLeft());
		numLeftView.setFontSize (14);
		numLeftView.setBounds (540 + ci.getWidth(), 0, 60, 40);
		container.addWidget (numLeftView);
		
	}

	private void initializeModel(int seed) {
		deck = new MultiDeck(2);
		deck.create(seed);
		model.addElement (deck);   // add to our model (as defined within our superclass).

		// each of the piles appears here
		for (int i = 1; i<=8; i++) {
			fives[i] = new Pile ("fives" + i);
			model.addElement (fives[i]);
		} 
		for (int i = 1; i<=8; i++) {
			sixes[i] = new Pile ("sixes" + i);
			model.addElement (sixes[i]);
		} 

		// develop Queen columns
		for (int i = 1; i<=8; i++) {
			queens[i] = new Column ("queens" + i);
			model.addElement (queens[i]);
		}

	}
	
	/** Code to launch solitaire variation. */
	public static void main (String []args) {
		// Seed is to ensure we get the same initial cards every time.
		// Here the seed is to "order by suit."
		Main.generateWindow(new Intrigue(), Deck.OrderBySuit);
		//Main.generateWindow(new Intrigue(), Deck.OrderByRank);

	}

}
