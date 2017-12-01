package com.geoida.progeo.Callbacks;

/**
 * Created by Kamil on 11.10.2017.
 * This interfece is an callback that will be called after completed generating process
 */

public interface OnGeneratingFinished {
    public enum State{success,error_no_routes,error_download}
    void OnGeneratingFinished(State status);
}
