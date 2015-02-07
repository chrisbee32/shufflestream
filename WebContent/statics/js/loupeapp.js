//---------------------------------------------------
//------ Loupe object for use around the app --------
//---------------------------------------------------
var loupe = (function () {
	var _getRootUrl = function() {
		return window.location.origin?window.location.origin+'/':window.location.protocol+'/'+window.location.host+'/';
	};

	var _currentRoot = _getRootUrl();

	var _bindEvents = function() {

	};

	var _initCarousels = function() {

	};

	var _getAllChannels = function() {
		return $.getJSON(_currentRoot + "getchannels");
	};

	var _getChannel = function(channel_id) {
		return $.getJSON(_currentRoot + "getcontent?channel="+channel_id);
	};

	var obj = {
		init: function() {
			_bindEvents();
		},
		initCarousels: function() {
			_initCarousels();
		},
		getAllChannels: function() {
			return _getAllChannels();
		},
		getChannel: function(channel_id) {
			return _getChannel(channel_id);
		},
		currentRoot: _currentRoot
	};

	return obj;
})();

$(document).ready(function(){
	loupe.init();
});


//--------------------------------------
//------ Ember application code --------
//--------------------------------------
App = Ember.Application.create();

App.Router.map(function() {
	this.resource('channels');
	this.resource('admin');
	this.resource('channel', { path: ':channel_id'});
});

App.IndexRoute = Ember.Route.extend({
	model: function () {
		return loupe.getAllChannels();
	}
});

App.ChannelsRoute = Ember.Route.extend({
	model: function () {
		return loupe.getAllChannels();
	}
});

App.ChannelRoute = Ember.Route.extend({
	model: function (params) {
		return loupe.getChannel(params.channel_id);
	}
});

App.ChannelView = Ember.View.extend({
	didInsertElement: function() {
		$(".loupe-reel").slick({
			slidesToShow: 1,
			adaptiveHeight: true,
			autoplay: true,
			autoplaySpeed: 3000
		});
	}
});

Ember.View.reopen({
  didInsertElement : function(){
    this._super();
    Ember.run.scheduleOnce('afterRender', this, this.afterRenderEvent);
  },
  afterRenderEvent : function(){
    // implement this hook in your own subclasses and run your jQuery logic there
  }
});