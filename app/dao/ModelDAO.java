package dao;

import uk.co.panaxiom.playjongo.PlayJongo;

/**
 * Created by jeromeheissler on 23/02/2017.
 */
public abstract class ModelDAO<T, U> {

    protected PlayJongo jongo;
    public ModelDAO(PlayJongo jongo) {
        this.jongo = jongo;
    }

    public abstract T findById(U id);
    public abstract void insert(T model);
    public abstract void update(T model);
    public abstract void remove(T model);

}
