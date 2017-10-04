
(function() {
	'use strict';

	rap.registerTypeHandler("fullcalendar.io", {

		factory : function(properties) {
			return new fullcalendar.FullCalendar(properties);
		},

		destructor : "destroy",

		properties : [ "context","update","add","remove"]

	});

	if (!window.fullcalendar) {
		window.fullcalendar = {};
	}

	fullcalendar.FullCalendar = function(properties) {
		bindAll(this, [ "layout", "onReady", "onSend", "onRender","event_select",'day_select',"event_drop",'event_resize' ]);
		this.parent = rap.getObject(properties.parent);
		
		this.element = document.createElement("div");
		this.element.id = 'calendar';
		this.element.style.height = '100%';
		this.element.style.overflow = 'auto';
		this.element.style.position = 'absolute';
		this.element.style.bottom = '0px';
		this.element.style.top = '0px';
		this.element.style.left = '0px';
		this.element.style.right = '0px';
		this.context = properties.context;
		this.cal =  null;

	
		
		

		this.parent.append(this.element);
		this.parent.addListener("Resize", this.layout);
		rap.on("render", this.onRender);
	};

	fullcalendar.FullCalendar.prototype = {

		ready : false,

		onReady : function() {
			
			
			
			
			this.layout();
			
		},

		onRender : function() {

			if (this.element.parentNode) {
				rap.off("render", this.onRender);
				
				this.ready = true;

				this.parentNode = this.element;
				this.setContext(this.context);
				
				rap.on("send", this.onSend);
			}
		},

		onSend : function() {
			
		},

		setContext : function(_context) {
			this.context = _context;
			
			if(this.ready )
			{
				this.context.eventClick = this.event_select;
				this.context.dayClick = this.day_select;
				this.context.eventDrop = this.event_drop;
				this.context.eventResize = this.event_resize;
				$('#calendar').fullCalendar(this.context);
			}
			
		},

		setUpdate : function(evt) {
			
			if(this.ready )
			{
				
				
				var result = $('#calendar').fullCalendar( 'clientEvents' ,evt.id ); 
				
				if(result && result[0])
				{
					var event = result[0];
					//copy
					for(var k in evt) event[k]=evt[k];
					$('#calendar').fullCalendar('updateEvent', event);
				}
				
			}
			
		},
        setAdd : function(evts) {
			
			if(this.ready )
			{
				
				
				$('#calendar').fullCalendar('addEventSource', evts);
				
			}
			
		},
        setRemove : function(evtId) {
			
			if(this.ready )
			{
				
				
				$('#calendar').fullCalendar('removeEvents', evtId);
				
			}
			
		},
	
	

		destroy : function() {
			if (this.parentNode.parentNode) {
				rap.off("send", this.onSend);
				
				this.element.parentNode.removeChild(this.element);
				
				
			}
		},

		layout : function() {
			if (this.ready) {
				
					
				if(this.context)
                {
                
                }
				
				
			}
		},
		event_select : function(evt) {
	    	
		
			var remoteObject = rap.getRemoteObject(this);
			var args = {id: evt.id};
	        
			remoteObject.call('event_select',args);
	        
	        
	    },
	    event_drop : function(evt, delta, revertFunc) {
	    	
	    	
	    	    var remoteObject = rap.getRemoteObject(this);
			   
	    	    var event = {};
		 	    event.id = evt.id;
		 	    event.title = evt.title;
		 	    event.allDay = evt.allDay;
		 	    event.start = evt.start;
		 	    event.end = evt.end;
		    	remoteObject.call('event_changed',event);
	    	
	    	
	    },
	    event_resize : function(evt, delta, revertFunc) {
	    	
	    	
		    	var remoteObject = rap.getRemoteObject(this);
		   
	 	     var event = {};
	 	    event.id = evt.id;
	 	    event.title = evt.title;
	 	    event.allDay = evt.allDay;
	 	    event.start = evt.start;
	 	    event.end = evt.end;
		    	remoteObject.call('event_changed',event);
	    	
	    	
	    },
	    day_select : function(date, jsEvent, view) {
	    	
	    	
	    	    var remoteObject = rap.getRemoteObject(this);
			var args = {select_date: date};
	        
			remoteObject.call('day_select',args);
	    	
	    	
	    },

	};

	var bind = function(context, method) {
		return function() {
			return method.apply(context, arguments);
		};
	};

	var bindAll = function(context, methodNames) {
		for (var i = 0; i < methodNames.length; i++) {
			var method = context[methodNames[i]];
			context[methodNames[i]] = bind(context, method);
		}
	};

	var async = function(context, func) {
		window.setTimeout(function() {
			func.apply(context);
		}, 0);
	};

}());
