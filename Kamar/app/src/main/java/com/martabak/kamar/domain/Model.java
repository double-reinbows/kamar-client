package com.martabak.kamar.domain;

/**
 * The abstract domain model class.
 */
public abstract class Model {

    public final String _id;

    public final String _rev;

    public final transient boolean dirty;

    public Model() {
        this._id = null;
        this._rev = null;
        this.dirty = false;
    }

    public Model(String _id, String _rev) {
        this._id = _id;
        this._rev = _rev;
        this.dirty = true;
    }

    @Override
    public int hashCode() {
        return _id != null ? _id.hashCode() : super.hashCode();
    }

}
