
function createEditor_terminal(divName,mode){
	var editor = ace.edit(divName);
	/////////////////////////////////////////////////////////////////////////////
	
	/////////////////////////////////////////////////////////////////////////////
	editor.setOptions({
	    enableBasicAutocompletion: false
	});
	editor.setTheme("ace/theme/monokai");
	if(mode){
		editor.session.setMode("ace/mode/"+mode);
	}else{
		editor.session.setMode("ace/mode/javascript");
	}
	
	editor.setValue("");
	
	//editor.commands.bindKey(".", "startAutocomplete") 
	//editor.commands.bindKey("ctrl-space", null) // do nothing on ctrl-space 
	
	editor.commands.on("exec", function(e){ 
		console.log("command:"+e.command.name);
		console.log("args:"+e.args);

		var content = editor.getValue();
		var rows = content.split('\n');
		var rowCount = rows.length;		
		
		if (e.command.name == "insertstring" && e.args== "\n") {
			editor.selection.moveTo(rowCount-1, rows[rowCount-1].length);
			return;
		}
		
		// can only append at last line
		if(e.command.name == "cut"
		    || e.command.name == "removeline"){
		    e.preventDefault();
		    e.stopPropagation();
			return;
		}
		
		if (e.command.name == "insertstring" 
			|| e.command.name == "backspace" 
			|| e.command.name == "del"
			|| e.command.name == "paste"
		) {
	        var cursor = editor.selection.getCursor();
			var row = cursor.row;
			var column = cursor.column;
			console.log("row:"+row);
			console.log("col:"+column);
			if(row < rowCount-1){
			    e.preventDefault();
			    e.stopPropagation();
			}
			
			if(row == rowCount-1){
				if(column < (currentPath + ">").length){
					e.preventDefault();
				    e.stopPropagation();
				}
				
				if(e.command.name == "backspace"){
					if(column < (currentPath + ">").length+1){
						e.preventDefault();
					    e.stopPropagation();
					}
				}

			}
	     } 

	}) 
	
	
	editor.commands.on("afterExec", function(e){ 
		//console.log("command:"+e.command.name);
		//console.log("args:"+e.args);

		if (e.command.name == "insertstring" && e.args== "\n") {
			
			
			var content = editor.getValue();
			var rows = content.split('\n');
			var rowCount = rows.length;		
			
			console.log(rows[rowCount-2]);
			
			if(rows[rowCount-2].indexOf(currentPath+">") == 0){
				var command = rows[rowCount-2].substring((currentPath+">").length);
		        
				console.log("send command:"+command);
				$.get(
            		"/command?word=" + encodeURIComponent(command) + "&apiId=" + $("#apiId").text(),
                function(data) {
					console.log("result:"+data);
					editor.insert(data);
                    editor.insert("\n"+currentPath+">",1);
                })
				.done(function() {  })
				.fail(function(jqXHR, textStatus, errorThrown) {
			        console.log("error " + textStatus);
			        console.log("incoming Text " + jqXHR.responseText);
			    })
				.always(function() {  });
					
			}

	     } 

/*		
		if (e.command.name == "insertstring" && e.args== ".") {
			window["dot"] = true;
	         editor.execCommand("startAutocomplete") 
	     } else if (e.command.name == "insertstring" && e.args != null && e.args.length > 0) { 
	    	 

	    	 if((e.args.charAt(0) >= "a" && e.args.charAt(0) <= "z") 
	    			 || (e.args.charAt(0) >= "A" && e.args.charAt(0) <= "Z") 
	    			 || (e.args.charAt(0) == "$")
			 ){
	    		 window["dot"] = false;
		         editor.execCommand("startAutocomplete") 
	    	 }else{
	    		 
	    	 }
    	 
	     } 
*/	
	     //if (e.command.name == "insertstring"&&/^[\w.]$/.test(e.args)) { 
	     //    editor.execCommand("startAutocomplete") 
	     //} 
	}) 
	
	editor.setOptions({
	    enableBasicAutocompletion: true
	});
	
	
	var langTools = ace.require("ace/ext/language_tools");
		
    // uses http://rhymebrain.com/api.html
    var rhymeCompleter = {
        getCompletions: function(editor, session, pos, prefix, callback) {
        	//console.log("prefix:"+prefix);
            //if (prefix.length === 0) { callback(null, []); return }
            
			var content = editor.getValue();
			var rows = content.split('\n');
			var rowStr = rows[pos.row];
			
			console.log(rowStr);
			console.log(pos);
        	
        	var keyWord = "";
			
			var i =pos.col-1;
			for( ; i >= 0; i --){
				var c = rowStr.charAt(i);
				if(
					   (c >= 'a' && c <= 'z')
					|| (c >= 'A' && c <= 'Z')
					|| (c >= '0' && c <= '9')
					|| (c == '_')
					|| (c == '$')
					|| (c == '#')
					|| (c == '.')
				){
					continue;
				}
				
				break;
			}
			
			keyWord = rowStr.substring(i,rowStr.length-1);

            $.getJSON(
            		"/complete?word=" + encodeURIComponent(keyWord),
            
            //getWordList(
            //    	editor.getValue(),
            //    	pos,
                function(wordList) {
                    // wordList like [{"word":"flow","freq":24,"score":300,"flags":"bc","syllables":"1"}]
                    callback(null, wordList.map(function(ea) {
                        return {caption: ea.word, name: ea.word, value: ea.word, score: ea.score, meta: "rhyme", 
                        	completer: {
		                        insertMatch: function (editor, data) {
		                            console.log("Item clicked: ", data.value);
		                            var str = data.value;
		                            var endPos = str.length;
		                            var target = str.substring(0,endPos);
		                            
		                            if(!window["dot"]){
		                            	editor.removeWordLeft();
		                            }
		                            
		                            editor.insert(str);
		                            
		                        }
		                    }
                        }
                    }));
                })
        }
    };
	
    //langTools.addCompleter(rhymeCompleter);
	langTools.setCompleters([rhymeCompleter]);
	
	return editor;
}