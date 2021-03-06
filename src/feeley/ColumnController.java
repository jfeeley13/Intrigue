package feeley;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import ks.common.controller.SolitaireReleasedAdapter;
import ks.common.games.Solitaire;
import ks.common.model.Card;
import ks.common.model.Column;
import ks.common.model.Move;
import ks.common.model.Pile;
import ks.common.view.CardView;
import ks.common.view.ColumnView;
import ks.common.view.Container;
import ks.common.view.PileView;
import ks.common.view.Widget;

public class ColumnController extends SolitaireReleasedAdapter {
	ColumnView src;
	
	public ColumnController(Solitaire theGame, ColumnView src) {
		super(theGame);
		this.src = src;
	}
	
	public void mousePressed(MouseEvent me) {
		 
		// The container manages several critical pieces of information; namely, it
		// is responsible for the draggingObject; in our case, this would be a CardView
		// Widget managing the card we are trying to drag between a column and piles.
		Container c = theGame.getContainer();
		
		/** Return if there is no card to be chosen. */
		Column queens = (Column) src.getModelElement();
		if (queens.count() == 1) {		//cannot pick up queen
			c.releaseDraggingObject();
			System.out.println ("Cannot pick up a queen");

			return;
		}
	
		// Get a card to move from ColumnView. Note: this returns a CardView.
		// Note that this method will alter the model for ColumnView if the condition is met.
		CardView cardView = src.getCardViewForTopCard (me);
		
		// an invalid selection of some sort.
		if (cardView == null) {
			c.releaseDraggingObject();
			return;
		}
		
		// If we get here, then the user has indeed clicked on the top card in the ColumnView and
		// we are able to now move it on the screen at will. For smooth action, the bounds for the
		// cardView widget reflect the original card location on the screen.
		Widget w = c.getActiveDraggingObject();
		if (w != Container.getNothingBeingDragged()) {
			System.err.println ("ColumnController::mousePressed(): Unexpectedly encountered a Dragging Object during a Mouse press.");
			return;
		}
	
		// Tell container which object is being dragged, and where in that widget the user clicked.
		c.setActiveDraggingObject (cardView, me);
		
		// Tell container which source widget initiated the drag
		c.setDragSource (src);
	
		// The only widget that could have changed is ourselves. If we called refresh, there
		// would be a flicker, because the dragged widget would not be redrawn. We simply
		// force the WastePile's image to be updated, but nothing is refreshed on the screen.
		// This is patently OK because the card has not yet been dragged away to reveal the
		// card beneath it.  A bit tricky and I like it!
		src.redraw();
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
			System.err.println ("ColumnController::mouseReleased(): somehow no dragSource in container.");
			c.releaseDraggingObject();
			return;
		}

		// coming from a col
		Column fromColumn = (Column) fromWidget.getModelElement();

		// Determine the toColumn
		Column toColumn = (Column) src.getModelElement();


		// Must be the CardView widget being dragged. 
		CardView cardView = (CardView) draggingWidget;
		Card theCard = (Card) cardView.getModelElement();
		if (theCard == null) {
			System.err.println ("ColumnController::mouseReleased(): somehow CardView model element is null.");
			c.releaseDraggingObject();			
			return;
		}
		Move m = new MoveCardCol (fromColumn, theCard, toColumn);

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
}
