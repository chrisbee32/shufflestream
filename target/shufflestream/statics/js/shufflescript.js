var channeldisplayedcount = 0; 

var shuffle = {
	
	init: function(){	
		env.init(); 
		var param = env.getParams("channel"); 
		$('#channelMgr option').filter(function () { return $(this).html() == param; }).attr("selected", "selected");
		
		$('#addChannel').click(function(){
			shuffle.addChannel();
		});
		
		$('#removeChannel').click(function(){
			shuffle.removeChannel();
		}); 
		
		$('#channelMgr').change(function(){
			var url = $('#channelMgr').val();			 
			window.location.href=url;			
		});
		
		$("#fileUploader").change(function(){
		    shuffle.showPreview(this);
		});	
		
	},
		
	addChannel: function(){
		var channelcount = $('input[name=channelcount]').val(); 
		var options = ''; 
		$('.Channels').each(function(index){
				channeldisplayedcount++; 
			}
		);
		if(channeldisplayedcount < channelcount){
			$('#Channels option').each(function(){
				options += ('<option value="' + $(this).val()+'">'+$(this).val() + '</option>'); 
			});
			$('#Channels').after('<p> <select name="Channels" id="Channels">'+ options + '</select></p>');	
		}
		else{
			alert("Image already added to all channels"); 
		}; 
	},	
	
	removeChannel: function(){
		$('.Channels').last().remove(); 
		channeldisplayedcount--; 
	},
	
	showPreview: function(input) {
	    if (input.files && input.files[0]) {
	        var reader = new FileReader();

	        reader.onload = function (e) {
	            $('#uploadPreview').attr('src', e.target.result);
	            $('#uploadPreview').show(); 
	        }

	        reader.readAsDataURL(input.files[0]);
	    }
	}	
}; 

var env = {
		init: function(callback){
			var qs = decodeURIComponent(window.location.search.substring(1)); 
			var qsh = decodeURIComponent(window.location.hash.substring(1)); //for chrome mobile
	
			if (qsh){
				qs = qsh.split("?")[1]; 
			}

			var qsparams = qs.split("&"); 
			var pairs = []; 
			$.each(qsparams, function(i, value){
				var qskey = qsparams[i].split("=")[0]; 
				var qsval = qsparams[i].split("=")[1]; 
				pairs[qskey] = qsval;  
			}); 
			env.params = pairs; 

			if (callback){
				callback(); 	
			}
		},

		getParams: function(keyName){
			if (keyName){
				return env.params[keyName];
			}
			else{
				return env.params; 
			}
		}
	};

$(document).ready(function(){
	shuffle.init(); 
}); 

