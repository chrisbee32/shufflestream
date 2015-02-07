function getRootUrl() {
	return window.location.origin?window.location.origin+'/':window.location.protocol+'/'+window.location.host+'/';
}


App = Ember.Application.create();

App.Router.map(function() {
	this.resource('channels');
	this.resource('admin');
	this.resource('channel', { path: ':channel_id'});
});

App.ChannelsRoute = Ember.Route.extend({
	model: function () {
		var root = getRootUrl();
		return $.getJSON(root + "getchannels");
	}
});

App.ChannelRoute = Ember.Route.extend({
	model: function (params) {
		var root = getRootUrl();
		return $.getJSON(root + "getcontent?channel="+params.channel_id);
	}
});