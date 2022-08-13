//url = "https://localhost:20452/stockapi/";
 let completeURL = window.location.href;
 let index = completeURL.indexOf("/bol");
 url = completeURL.substring(0,index) + '/stockapi/';
//url = window.location.href ;

  function formRequest(methodType,api)
  {
        let request = new XMLHttpRequest () ;
        request.open(methodType,api,false);
        request.setRequestHeader("Content-type", "application/json");
        request.setRequestHeader("Access-Control-Allow-Origin", url);
        console.log(api);
        return request ;
  }

function submitRunQry()
{


}

function addAnd()
{
  let tabl = document.getElementById('qryTable');
  var rowCount = tabl.rows.length;
  var row = tabl.rows[rowCount-1];
  var newrow = tabl.insertRow();
  newrow.innerHTML = row.innerHTML;
  var inputs = newrow.getElementsByTagName('input');
  console.log('RowCount=' + rowCount);
  for (i = 0; i < inputs.length; i++) {
     inputs[i].value = "";
  }
    document.getElementsByName("spnOper")[rowCount-1].innerHTML = 'AND';

}

function clearQry()
{

  let tabl = document.getElementById('qryTable');
  var lastrow  =tabl.rows[1];
  while ( document.getElementsByName("spnOper").length > 1 ) {
        console.log("i="  + i);
        console.log("len="  + document.getElementsByName("spnOper").length);
          tabl.deleteRow(tabl.rows.length - 1);
   }
  var inputs = lastrow.getElementsByTagName('input');
     console.log('inputs= ' + inputs);
     for (var i = 0; i < inputs.length; i++) {
        inputs[i].value = "";
     }

}

function populateResults()
{

}

function addOr()
{
   let tabl = document.getElementById('qryTable');
   var rowCount = tabl.rows.length;
   console.log('RowCount=' + rowCount);
   var row = tabl.rows[rowCount-1];
   var newrow = tabl.insertRow();
   newrow.innerHTML = row.innerHTML;
   var inputs = newrow.getElementsByTagName('input');
   for (i = 0; i < inputs.length; i++) {
      inputs[i].value = "";
   }
   document.getElementsByName("spnOper")[rowCount-1].innerHTML = 'OR';

}

function setToken (request)
{
    var allcookies = document.cookie;
    cookiearray = allcookies.split(';');
    for(var i=0; i<cookiearray.length; i++) {
      name = cookiearray[i].split('=')[0];
      value = cookiearray[i].split('=')[1];
      console.log ("Key is : " + name + " and Value is : " + value);
      if(name.trim() =='XSRF-TOKEN') {
          request.setRequestHeader("X-XSRF-TOKEN", value);
          console.log('added x-xsrf-token');
          }
    }
}

function buildQuery()
{
  var jsonArr  =  [];
  let tabl = document.getElementById('qryTable');
  var rowCount = tabl.rows.length;
  for ( var i = 0; i < rowCount-1 ; i ++ )  {
     var json = {} ;
     var cond = document.getElementsByName("spnOper")[i].innerHTML ;
     json['condition'] = cond ;
     var field = document.getElementsByName("qryField")[i].value;
     json['property'] = field ;
     var operator = document.getElementsByName("qryOP")[i].value;
     json['operator'] = operator ;
     var value = document.getElementsByName("txtValue")[i].value;
     json['value'] = value ;
     console.log(json);
     jsonArr.push(json);
  }

    console.log(jsonArr);
    var fullurl = url + 'query/runQuery' ;
    let request = formRequest("POST",fullurl);
    setToken(request);
    request.send(JSON.stringify(jsonArr),true) ;
    var snapsotresponse  =   JSON.parse(request.responseText)  ;
    console.log(snapsotresponse);
    reRenderTable('resultsTable',snapsotresponse);
}


 function reRenderTable(tableId,data)
 {
         var dataTable = document.getElementById(tableId);
         while (dataTable.rows.length > 1) {
                 dataTable.deleteRow(dataTable.rows.length -1 );
         }

         for ( var i in data) {
                singleRow=  data[i];
                //innerContent = "<tr>";
                var bgColor = i%2==0?'beige':'white';
                innerContent = '<tr data-row="' + bgColor +  '">';
                innerContent =  innerContent + '<td><a href="../stockDetails.html?bseCode='+ singleRow['bseCode'] +'">' + singleRow['bseCode'] + '</a></td>';
                innerContent =  innerContent + '<td>' + singleRow['stock'] + '</td>';
                innerContent =  innerContent + '<td>' + singleRow['industry'] + '</td>';
                innerContent =  innerContent + '<td>' + singleRow['sector'] + '</td>';
                innerContent =  innerContent + '<td>' + singleRow['group'] + '</td>';
                innerContent =  innerContent + '<td>' + singleRow['marketCap'] + '</td>';
                innerContent =  innerContent + '<td>' + singleRow['groupCap'] + '</td>';
                innerContent =  innerContent + '<td>' + singleRow['currentPrice'] + '</td>';
                innerContent =  innerContent + '<td>' + singleRow['eps'] + '</td>';
                innerContent =  innerContent + '<td>' + singleRow['bookvalue'] + '</td>';
                innerContent =  innerContent + '<td>' + singleRow['revenue'] + '</td>';
                innerContent =  innerContent + '<td>' + singleRow['profit'] + '</td>';
                innerContent =  innerContent + '<td>' + singleRow['divident'] + '</td>';
                innerContent =  innerContent + '<td>' + singleRow['pe'] + '</td>';
                innerContent =  innerContent + '<td>' + singleRow['pb'] + '</td>';
                innerContent =  innerContent + '<td>' + singleRow['roe'] + '</td>';
                innerContent =  innerContent + '<td>' + singleRow['dividentYield'] + '</td>';
                innerContent =  innerContent + '<td>' + singleRow['profitIncr'] + '</td>';
                innerContent =  innerContent + '</tr>';
                 var newrow = dataTable.insertRow();
                 newrow.innerHTML =  innerContent;
             }
            applyColor();

 }

 function applyColor()
  {
     $("tr:odd").css("background-color", "white");
     $("tr:even").css("background-color", "aliceblue");
  }


function getLookupWithAjax( currentCtrl,dataListCtrlName)
{
    var srValue = currentCtrl.value ;
  	if(srValue.length  > 2 || srValue.indexOf('*') !=  -1 ) {

    var index  = getCurrentObjectIndex(currentCtrl);
    console.log( "index" + index) ;
    var lookupTypeVal = document.getElementsByName("qryField")[index].value ;
    var lookupType ='Industry' ;
    if (lookupTypeVal == 'SCT')
        lookupType ='Sector' ;

	currentCtrl.autocomplete ="on";

	var requestStr = url + "uiapi/lookup?lookupType=" + lookupType
	+ "&searchStr=" + srValue ;

	var reqObject = new XMLHttpRequest();
	reqObject.open("GET",requestStr,false);
	reqObject.send();
	console.log("Resp" + reqObject.responseText);

	var elem = document.getElementsByName(dataListCtrlName)[0];
	console.log ('before' + elem.innerHTML) ;
 	elem.innerHTML='';
	 var options = '';
	var propArray =  JSON.parse(reqObject.responseText) ;
    var found =false;
	for (var key in propArray) {
		  var value = propArray[key];
		  options += '<option  value="'+value+'" />' + key + '</option>';
		  found =true ;
	}
	if (found == false) {
		currentCtrl.autocomplete ="off";
	}
	elem.innerHTML=  options;
	console.log ('after' + elem.innerHTML) ;
	}

}

function getCurrentObjectIndex(currentCtrl)  {
	var objName = currentCtrl.name;
	var elemCount = document.getElementsByName(objName).length;
	console.log("getCurrentObjectIndex:elemCount= " + elemCount  + ":objName="  + objName);
	for (var i = 0 ; i < elemCount ; i ++ ) {
		if (document.getElementsByName(objName)[i]  == 	currentCtrl)  {
			return i ;
		}
	}
	return  0 ;
}
