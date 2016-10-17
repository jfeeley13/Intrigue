package feeley;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import ks.common.controller.SolitaireReleasedAdapter;
import ks.common.games.Solitaire;
import ks.common.model.BuildablePile;
import ks.common.model.Card;
import ks.common.model.Column;
import ks.common.model.Move;
import ks.common.model.Pile;
import ks.common.view.BuildablePileView;
import ks.common.view.CardView;
import ks.common.view.ColumnView;
import ks.common.view.Container;
import ks.common.view.PileView;
import ks.common.view.Widget;

public class PileController extends SolitaireReleasedAdapter {
	PileView src;
	String kind;

	public PileController(Solitaire theGame, PileView pilesView, String pilekind) {
		super(theGame);
		this.src = pilesView;
		this.kind = pilekind;
	}

	public void mouseReleased(MouseEvent me) {
		Container c = theGame.getContainer();

		/** Return if there is no card being dragged chosen. */
		Widget draggingWidget = c.getActiveDraggingObject();
		if (draggingWidget == Container.getNothingBeingDragged()) {
			System.out.println ("PileController::mouseReleased() unexpectedly found nothing being dragged.");
			c.releaseDraggingObject();		
			return;
		}

		/** Recover the from Pile */
		Widget fromWidget = c.getDragSource();
		if (fromWidget == null) {
			System.err.println ("PileController::mouseReleased(): somehow no dragSource in container.");
			c.releaseDraggingObject();
			return;
		}

		// Determine the To Pile
		Pile foundation = (Pile) src.getModelElement();
		
		// coming from a Column [user may be trying to move multiple cards]
		// coming from a Pile or col

		
		Column fromColumn = (Column) fromWidget.getModelElement();

		// Must be the CardView widget being dragged. 
		CardView cardView = (CardView) draggingWidget;
		Card theCard = (Card) cardView.getModelElement();
		if (theCard == null) {
			System.err.println ("PileController::mouseReleased(): somehow CardView model element is null.");
			c.releaseDraggingObject();			
			return;
		}

		Move m = new MoveCard (fromColumn, theCard, foundation, kind);

		if (m.doMove (theGame)) {
			theGame.pushMove (m);
		} else {
			fromWidget.returnWidget (draggingWidget);
		}

		// release the dragging object, (this will reset dragSource)
		c.releaseDraggingObject();

		// finally repaint
		c.repaint();
	}


	//cannot move card in pile
	public void mousePressed(MouseEvent me) {

	/*	Container c = theGame.getContainer();

		// Return if there is no card to be chosen. 
		Pile pile = (Pile) src.getModelElement();
		if (pile.count() == 0) {
			c.releaseDraggingObject();
			return;
		}

		// Get a card to move from PileView. Note: this returns a CardView.
		// Note that this method will alter the model for PileView if the condition is met.
		CardView cardView = src.getCardViewForTopCard (me);

		// an invalid selection of some sort.
		if (cardView == null) {
			c.releaseDraggingObject();
			return;
		}

		// If we get here, then the user has indeed clicked on the top card in the PileView and
		// we are able to now move it on the screen at will. For smooth action, the bounds for the
		// cardView widget reflect the original card location on the screen.
		Widget w = c.getActiveDraggingObject();
		if (w != Container.getNothingBeingDragged()) {
			System.err.println ("FivesPileController::mousePressed(): Unexpectedly encountered a Dragging Object during a Mouse press.");
			return;
		}

		// Tell container which object is being dragged, and where in that widget the user clicked.
		c.setActiveDraggingObject (cardView, me);

		// Tell container which source widget initiated the drag
		c.setDragSource (src);

		src.redraw();
	}
	*/
	}

	}
