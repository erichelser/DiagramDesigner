import java.awt.event.*;

abstract class ClickableBox extends Tangible
{
	private OrderedPair size;
	private OrderedPair position; //of upper left corner

	protected ClickableBox()
	{
		super();
		size = new OrderedPair();
		position = new OrderedPair();
	}

	public boolean isClicked(MouseEvent e)
	{
		OrderedPair boardPos = getBoard().pixelToPos(new OrderedPair(e.getX(), e.getY()));
		boardPos.subtract(getPosition());
		return (0 <= boardPos.getX() && boardPos.getX() <= getSize().getX()
			 && 0 <= boardPos.getY() && boardPos.getY() <= getSize().getY());
	}

	public void setSize(OrderedPair s) { size.set(s); }
	public OrderedPair getSize() { return size; }

	public void setPosition(OrderedPair p) { position.set(p); }
	public OrderedPair getPosition() { return position; }

	public OrderedPair getCenterPosition()
	{
		if (size == null || position == null)
			return getPosition();
		return getPosition().replicate().add(getSize().getHalf());
	}
}
