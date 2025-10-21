package wargame;

public class Element {
	private Object o;
	private Position p;
	public Element(object o,Position po) {
		this.o = o;
		this.p = new Position(po.getX(), po.getY());
	}
	public Element(object o,int x, int y) {
		this.o = o;
		this.p = new Position(x, y);
	}
	public object getElement(){
		return o;
	}
	public void setPosition(Position po) {
		this.p = new Position(po.getX(), po.getY());
	}
	public String toString() {
		message = "";
		message = message + "Object o : " + "pos x :" + p.getX() + "pos y :" + p.getY();
	}
}