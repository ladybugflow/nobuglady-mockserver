
function createEditor_help(divName,mode){
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
	
	// disable syntax check
	editor.getSession().setUseWorker(false);
	
	editor.setValue(`
/***************************************/
/* Structure of local varible:response */
/***************************************/
response:{
	status:int, //default:200
	header:{
		key1:value1,
		key2:value2,
		...
	},
	cookie:[
		{
			name:String,
			value:String,
			version:int, //default:0
			comment:String,
			domain:String,
			maxAge:int, //default:-1
			path:String,
			secure:Boolean,
			httpOnly:Boolean,
		}
	],
	body:String
}

/***************************************/
/* Structure of local varible:request  */
/***************************************/
request:{
	sessionId:String,
	method:String,
	url:String,
	header:{
		key1:value1,
		key2:value2,
		...
	},
	cookie:[
		{
			name:String,
			value:String,
			version:int, // default:0
			comment:String,
			domain:String,
			maxAge:int, // default:-1
			path:String,
			secure:Boolean,
			httpOnly:Boolean,
		}
	],
	parameter:{
		key1:[value1,value2,....],
		key2:[value1,value2,....],
		...
	},
	body:String
}

/***************************************/
/* Structure of local varible:console  */
/***************************************/
console:{
	log(msg:String);
}

/***************************************/
/* Structure of local varible:cache    */
/***************************************/
cache:{
	saveCache(key:String,value:Object);
	getCache(key:String):Object;
	clearCache(key:String);
	clearAll();
}

/***************************************/
/* Structure of local varible:db       */
/***************************************/
db:{
	query(sql:String):[{colName1:colValue1,colName2:colValue2...}];
	update(sql:String):affectCount;
}

/***************************************/
/* Structure of local varible:file     */
/***************************************/
file:{
	write(path:String,content:String);
	read(path:String):String;
}

/***************************************/
/* Structure of local varible:thread   */
/***************************************/
thread:{
	sleep(millis:long);
}
	`,1);
	
	//editor.commands.bindKey(".", "startAutocomplete") 
	//editor.commands.bindKey("ctrl-space", null) // do nothing on ctrl-space 
	
	editor.commands.on("afterExec", function(e){ 
		//console.log("command:"+e.command.name);
		//console.log("args:"+e.args);
		
		if (e.command.name == "insertstring" && e.args== ".") {
			window["dot"] = true;
	         editor.execCommand("startAutocomplete") 
	     } else if (e.command.name == "insertstring" && e.args != null && e.args.length > 0) { 
	    	 
/*
	    	 if((e.args.charAt(0) >= "a" && e.args.charAt(0) <= "z") 
	    			 || (e.args.charAt(0) >= "A" && e.args.charAt(0) <= "Z") 
	    			 || (e.args.charAt(0) == "$")
			 ){
	    		 window["dot"] = false;
		         editor.execCommand("startAutocomplete") 
	    	 }else{
	    		 
	    	 }
*/	    	 
	     } 
		
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