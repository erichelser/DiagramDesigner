package DiagramDesigner;

public abstract class DraggableBox extends ClickableBox
{
	private OrderedPair delta; //temporary variables for when it's being dragged, in pixels
	private Bucket parentContainer;

	protected DraggableBox()
	{
		super();
		delta = new OrderedPair(0, 0);
		parentContainer = null;
	}

	public void setPixelDelta(OrderedPair d)
	{
		if(parentContainer!=null)
			parentContainer.setPixelDelta(d);
		else
			delta.set(d);
	}
	public OrderedPair getPixelDelta()
	{
		if (parentContainer != null)
			return parentContainer.getPixelDelta();
		return delta;
	}
	public Board getBoard()
	{
		if (parentContainer != null)
			return parentContainer.getBoard();
		return super.getBoard();
	}
	public void linkToParentContainer(Bucket b)
	{
		parentContainer = b;
	}
	public void applyPixelDelta()
	{
		if (parentContainer != null)
			parentContainer.applyPixelDelta();
		else
		{
			getPosition().add(getPixelDelta().multiply(1 / getBoard().getZoomFactor()));
			delta.setZero();
		}
	}
}