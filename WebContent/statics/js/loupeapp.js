//---------------------------------------------------
//------ Loupe object for use around the app --------
//---------------------------------------------------
var loupe = (function () {
	var $playButton, $pauseButton;
	var _initialized = false;

	var _getRootUrl = function() {
		return window.location.origin?window.location.origin+'/':window.location.protocol+'/'+window.location.host+'/';
	};

	var _currentRoot = _getRootUrl();

	var _bindMosiacEvents = function() {
		$(document).on("click",".featured-channel-bg-img", function() {
			_launchIntoFullscreen(document.documentElement); // the whole page
		});
	};

	var _bindChannelEvents = function() {
		$playButton = $("#loupe-reel-play-button");
		$pauseButton = $("#loupe-reel-pause-button");
		$infoButton = $("#loupe-reel-info-button");
		$currentInfo = $(".current-image-information");

		$playButton.on("click", function () {
			$pauseButton.show();
			$(this).hide();
			$(".loupe-reel").slick("slickPlay");
		});

		$pauseButton.on("click", function () {
			$playButton.show();
			$(this).hide();
			$(".loupe-reel").slick("slickPause");
		});

		$infoButton.on("click", function () {
			$playButton.show();
			$pauseButton.hide();
			$(".loupe-reel").slick("slickPause");
			$currentInfo.toggleClass("active");
		});

		$(".loupe-reel").on("click", function(event) {
			if (!$(event.target).closest('.current-image-information').length) {
				if ($currentInfo.hasClass("active")) {
					$currentInfo.removeClass("active");
				}
			}
		});
	};

	var _launchIntoFullscreen = function (element) {
		if(element.requestFullscreen) {
			element.requestFullscreen();
		} else if(element.mozRequestFullScreen) {
			element.mozRequestFullScreen();
		} else if(element.webkitRequestFullScreen) {
			element.webkitRequestFullScreen();
		} else if(element.msRequestFullscreen) {
			element.msRequestFullscreen();
		}
	};

	var _destroyLoupeReel = function() {
		$(".loupe-reel").slick("unslick");
		$(".loupe-reel-nav").slick("unslick");
	};

	var _initLoupeReel = function() {
		$(".loupe-reel").slick({
			slidesToShow: 1,
			nextArrow: $("#loupe-reel-next-button"),
			prevArrow: $("#loupe-reel-prev-button"),
			pauseOnHover: false,
			autoplay: true,
			fade: true,
			autoplaySpeed: 8000,
			asNavFor: '.loupe-reel-nav',
			lazyLoad: 'ondemand'
		});

		$(".loupe-reel-nav").slick({
			slidesToShow: 5,
			arrows: false,
			asNavFor: '.loupe-reel',
			centerMode: true,
			focusOnSelect: true,
			lazyLoad: 'ondemand'
		});	

		_populateCurrentImageData($(".loupe-reel .slick-slide[data-slick-index='0'] .loupe-img"));

		$('.loupe-reel').on('beforeChange', function(event, slick, currentSlide, nextSlide){
			var next = $(this).find(".slick-slide[data-slick-index='" + nextSlide + "'] .loupe-img");
			_populateCurrentImageData($(this).find(next));

			if(next.width() > next.height()) {
				$(".loupe-app-container").removeClass("portrait");
			}
			else {
				$(".loupe-app-container").addClass("portrait");
			}

		});
	};

	var _populateCurrentImageData = function ($image) {
		$(".current-image-information .current-image-name").text($image.data("title"));
		$(".current-image-information .current-artist").text($image.data("artist"));
		$(".current-image-information .current-description").text($image.data("description"));
		$(".current-image-information .current-artist-website").text($image.data("artist-website"));
	};

	var _removeCurrentImageData = function () {
		$(".current-image-information .current-image-name").text("");
		$(".current-image-information .current-artist").text("");
		$(".current-image-information .current-description").text("");
		$(".current-image-information .current-artist-website").text("");
	};

	var _getAllChannels = function() {
		$.when( $.ajax({
			url: loupe.currentRoot + "getchannels",
			async: true
		})).then(function (results) {
			var data = results;
			$.each(data, function(i, obj) {
				obj.thumbnailUrl = "background-image: url('"+obj.thumbnailUrl+"')";
				loupe.channels.pushObject(obj);
			});
		});
	};

	var _getChannel = function(channel_id) {
		// $.when( $.ajax({
		// 	url: loupe.currentRoot + "getcontent?channel="+channel_id,
		// 	async: true
		// })).then(function (results) {
		// 	var data = results;
		// 	$.each(data, function(i, obj) {
		// 		obj.attributes = JSON.stringify(obj.attributes);
		// 		loupe.currentChannel.pushObject(obj);
		// 	});
		// });
return $.getJSON(_currentRoot + "getcontent?channel="+channel_id);
};

var obj = {
	init: function() {
		if(!_initialized) {
			_initialized = true;
			return _getAllChannels();
		}	
	},
	initChannelPage: function() {
		_initLoupeReel();
		_bindChannelEvents();
		$(".preload").removeClass("preload");
	},
	bindMosiacEvents: function () {
		_bindMosiacEvents();
	},
	destroyChannelPage: function() {
		_destroyLoupeReel();
		_removeCurrentImageData();
		$(".loupe-app-container").addClass("preload");
	},
	getAllChannels: function() {
		return _getAllChannels();
	},
	getChannel: function(channel_id) {
		return _getChannel(channel_id);
	},
	currentRoot: _currentRoot,
	channels: [],
	currentChannel: []
};

return obj;
})();

$(window).load(function(){
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
		loupe.init();

		return loupe.channels;
	},
	actions: {
		willTransition: function(transition) {
			$(".loupe-app-container").addClass("preload");
		},
		deactivate: function() {
			$(".loupe-app-container").addClass("preload");
		}
	}
});

// App.ChannelsRoute = Ember.Route.extend({
// 	model: function () {
// 		return loupe.getAllChannels();
// 	}
// });

App.ChannelRoute = Ember.Route.extend({
	model: function (params) {
		return loupe.getChannel(params.channel_id);
	},
	actions: {
		willTransition: function(transition) {
			loupe.destroyChannelPage();
		},
		deactivate: function() {
			loupe.destroyChannelPage();
		}
	}
});

App.IndexView = Ember.View.extend({
	afterRenderEvent: function() {		
		$(".preload").removeClass("preload");	
		loupe.bindMosiacEvents();
	}
});

App.ChannelView = Ember.View.extend({
	afterRenderEvent: function() {
		loupe.initChannelPage();
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


(function() {
	Ember.Handlebars.registerHelper('bind-style', bindStyleHelper);

	Ember.Handlebars.registerHelper('bindStyle', function() {
		Em.warn("The 'bindStyle' view helper is deprecated in favor of 'bind-style'");
		return bindStyleHelper.apply(this, arguments);
	});

	function bindStyleHelper(options) {
		var fmt = Ember.String.fmt;
		var attrs = options.hash;

		Ember.assert("You must specify at least one hash argument to bindStyle", !!Ember.keys(attrs).length);

		var view = options.data.view;
		var ret = [];
		var style = [];

        // Generate a unique id for this element. This will be added as a
        // data attribute to the element so it can be looked up when
        // the bound property changes.
        var dataId = Ember.uuid();

        var attrKeys = Ember.keys(attrs).filter(function(item, index, self) {
        	return (item.indexOf("unit") == -1) && (item !== "static");
        });

        // For each attribute passed, create an observer and emit the
        // current value of the property as an attribute.
        attrKeys.forEach(function(attr) {
        	var property = attrs[attr];

        	Ember.assert(fmt("You must provide an expression as the value of bound attribute." +
        		" You specified: %@=%@", [attr, property]), typeof property === 'string');

        	var propertyUnit = attrs[attr+"-unit"] || attrs.unit || '';

        	var lazyValue = view.getStream(property);
        	var value = lazyValue.value();

        	Ember.assert(fmt("Attributes must be numbers, strings or booleans, not %@", [value]), value === null || typeof value === 'number' || typeof value === 'string' || typeof value === 'boolean');

        	lazyValue.subscribe(view._wrapAsScheduled(function applyAttributeBindings() {
        		var result = lazyValue.value();

        		Ember.assert(fmt("Attributes must be numbers, strings or booleans, not %@", [result]),
        			result === null || result === undefined || typeof result === 'number' ||
        			typeof result === 'string' || typeof result === 'boolean');

        		var elem = view.$("[data-bindattr-" + dataId + "='" + dataId + "']");

        		Ember.assert("An style binding was triggered when the element was not in the DOM", elem && elem.length !== 0);

        		elem.css(attr, result + "" + propertyUnit);
        	}));

        	if (attr === 'background-image' && typeof value === 'string' && value.substr(0, 4) !== 'url(') {
        		value = 'url(' + value + ')';
        	}

        	style.push(attr+':'+value+propertyUnit+';'+(attrs.static || ''));
        }, this);

        // Add the unique identifier
        ret.push('style="' + style.join(' ') + '" data-bindAttr-' + dataId + '="' + dataId + '"');
        return new Ember.Handlebars.SafeString(ret.join(' '));
    }
})();