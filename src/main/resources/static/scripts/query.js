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