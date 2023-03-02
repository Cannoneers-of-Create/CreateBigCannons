package rbasamoyai.createbigcannons.cannons;

public interface ICannonBlockEntity<T extends CannonBehavior> {

	T cannonBehavior();

}
