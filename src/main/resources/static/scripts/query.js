 url = "https://localhost:20452/";

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



}