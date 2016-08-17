package DiagramDesigner;

public class Periodic implements Runnable
{
	private boolean running;
	private double frequency;
	private Command command;
	public Periodic(double freq, Command c)
	{
		frequency = freq;
		command=c;
		running = true;
	}
	public void start()
	{
		(new Thread(this)).start();
	}
	public void run()
	{
		while (running)
		{
			try { Thread.sleep((int)(1000 / frequency)); }
			catch (InterruptedException e) { }
			command.execute();
		}
	}
	public void stop()
	{
		running = false;
	}
}
