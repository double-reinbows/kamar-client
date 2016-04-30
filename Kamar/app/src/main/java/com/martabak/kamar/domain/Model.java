package com.martabak.kamar.domain;

/**
 * The abstract domain model class.
 */
public abstract class Model {

    public final String _id;

    public final boolean dirty;

    public Model() {
        this._id = null;
        this.dirty = false;
    }

    public Model(String _id) {
        this._id = _id;
        this.dirty = true;
    }

}
