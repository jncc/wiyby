/* global L */

L.Control.Messagebox = L.Control.extend({
    options: {
        position: 'topleft'
    },

    onAdd: function (map) {
        this._container = L.DomUtil.create('div', 'leaflet-control-layers leaflet-control-layers-expanded leaflet-control');
        L.DomEvent.disableClickPropagation(this._container);
        return this._container;
    },

    show: function (message) {
        var elem = this._container;
        elem.innerHTML = message;
        elem.style.display = 'block';
    },
    
    hide: function () {
        var elem = this._container;
        elem.style.display = 'none';
    }
});

L.Map.mergeOptions({
    messagebox: false
});

L.Map.addInitHook(function () {
    if (this.options.messagebox) {
        this.messagebox = new L.Control.Messagebox();
        this.addControl(this.messagebox);
    }
});

L.control.messagebox = function (options) {
    return new L.Control.Messagebox(options);
};
