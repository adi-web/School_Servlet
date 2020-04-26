
const $tableID = $('#tabl');
var idputC;
var idClasse;
var reload; //variabile per salvare la classe in modo per fare la stampa deis studenti quando si modifica/aggiunge
window.onload = function ()//viene chiamata appena entri nella pagina per visualizzare le classi
 {
     
paginaC();
   

}

function paginaC() //ricarcia la pagina iniziale delle classi
{

    var xhr=new XMLHttpRequest();
    xhr.open("GET",'http://localhost:8080/School_Servlet/Get_Class',true);
    xhr.send();
    xhr.onload=function()
    { 
       
        $('#insertS').attr("data-target","#InserisciClasse");  //cambio data-target per il modal in modo da inserire la classe
        $('#insertS').attr("id","insert"); // button + uttilizzato sia per inserire student/class
        $('#thead').empty(); // elimina thead in modo da inserire le altre record altrimenti append li sovrascrive
        $('#titleT').html("Elenco Classi"); //chiamo nome tabella
        var data =JSON.parse(xhr.response);
        $('#thead').append('<tr style=" "><th style="display:none;" class="text-center">id</th> <th class="text-center">Year</th> <th class="text-center">Class</th>'+
        ' <th class="text-center" style="width: 145px;">Stampa studenti</th>'+
        '<th class="text-center" style="width: 100px;">Modifica </th>'+
        '<th class="text-center" style="width: 100px;">Elimina </th> </tr>');
    
        for(var i = 0; i <data.classInfo.length; i++)
        { 
            
            
                $('#thead').append('<tr><td style="display:none;" class=pt-3-half id="idD">'+data.classInfo[i].id +'</td><td class=pt-3-half>'+ data.classInfo[i].year+'</td>'+'<td class=pt-3-half>'+ data.classInfo[i].section+'</td>'+
                '<td><span class="table-visualizza" > <button type="button" style="border-radius: 10px ;color:blue"  id="adi">Stampa studenti</button></span> </td>'+
                '<td><span class="table-modifica" >  <button  data-toggle="modal" style="border-radius: 10px;color:green" data-target="#ModificaClasse" ><span class="glyphicon glyphicon-edit"></span></button></span> </td>'+
                '<td><span class="table-remove" id=elimina > <button type="button"  style="border-radius: 13px;color:red; " id=elimina><span class="glyphicon glyphicon-trash"></span></button></span> </td></tr>');
    }


}

    xhr.onerror=function()
    {
    alert("errore");
    }

}

$tableID.on("click", ".table-visualizza", function ()  //click visualizza studenti manda la tabella dei studenti per classe
{
    idputC=trovaID(this); //utilizzato per prendere id classe
    reload=trovaID(this);//utilizzato per prendere id classe utilizzato per fare il ricarimento della tabella studenti
    getStudent();

})

$tableID.on("click", "#insertS", function () //ciclk per inserire studente
{
    $('#Binserisci').attr("onclick","insertS()");//cambia onclick button nel modal inserimento studente in modo da chiamare funzione per l'inserimento
    $('#insertS').attr("data-target","#MStudente")//imposta data-target in modo quando click button insert+ chiama il modal MStudente
})


$tableID.on("click", ".table-modifica", function () // per la modifica class
{

    idputC=trovaID(this);
    

})

function classID(idputC)
{

   
    var xhr=new XMLHttpRequest();
    xhr.open("GET",'http://localhost/jquery/M_Classes.php/'+idputC,true);
    xhr.send();
   


xhr.onload=function()
{
    
    var data =JSON.parse(xhr.response);
    if(data.status==true)
    {   
        document.getElementById("year").value=data.classInfo.year;
        document.getElementById("class").value=data.classInfo.section;
    }
    else
    {
        alert("record non presente");
    }

};

xhr.onerror=function()
{
alert("errore");
}

}
$("#thead").on("click", "#mStudente", function () // per la modifica student
{
    $('#Binserisci').attr("onclick","modificaS()"); //imposta onclick button del modal inserimentostudent 
  
    idputC=trovaID(this);


})

$tableID.on("click", ".add", function () 
{
    idputC=trovaID(this);


})

$("#thead").on("click", ".table-remove", function () //per rimuovere class
{

    idputC=trovaID(this);
    deleteC(idputC,$(this));
});
$("#thead").on("click", ".table-removeS", function () //per rimuovere student
{

    idputC=trovaID(this);
    deleteS(idputC,$(this));
})


function deleteC(classId,thi)//elimina class
{
    var domanda = confirm("Sei sicuro di voler cancellare la classe?"); //finestra per conferma 
    if (domanda === true)
    {
    var id=classId;
    var xhr=new XMLHttpRequest();
    xhr.open("DELETE",'http://localhost:8080/School_Servlet/Get_Class?id='+id,true);
    xhr.send();   
xhr.onload=function()
{
    
    var data =JSON.parse(xhr.response);
    
    if(data.status==true)
    {   
        
        $(thi).parents('tr').detach(); //passato $(this) dalla funzione deleteC per capire cosa eliminare
       
    }
    else if(data.status==false)
    {

       alert("errore eliminazione");
    }

}
    }
        xhr.onerror=function()
        {
        alert("errore");
        }
}

function trovaID(thi) //trova l'id della tabella selezionata
 {
    var id=$(thi).closest("tr");// trova i tr 
    var classId=id.find("td:eq(0)").html(); //trova il valore della tabella
  
    return classId;
 }
 function modifica()//modifica class
 {

    var xhr=new XMLHttpRequest();
    xhr.open("PUT",'http://localhost:8080/School_Servlet/Get_Class',true);
    xhr.send(JSON.stringify({
        "id":idputC,
        "year":document.getElementById("year").value,
        "section":document.getElementById("class").value,
        }));
        xhr.onload=function()
     {
     
     var data =JSON.parse(xhr.response);
     if(data.status==true)
     { 
        paginaC(); //ricarica pagina class per visualizzare le modifiche
        $('#InserisciClasse').hide();
        //alert('Inserimento fatto');
     }
     else
     {
        alert('Errore inserimento ');
     }
 
 }
 

 }

 function insertC() //inserisce class
 {
 
     var xhr=new XMLHttpRequest();
     xhr.open("POST",'http://localhost:8080/School_Servlet/Get_Class',true);
     xhr.send(JSON.stringify({
         "year":document.getElementById("Iyear").value,
         "section":document.getElementById("Iclass").value,
         
         }));
      
 xhr.onload=function()
     {
     
     var data =JSON.parse(xhr.response);
     if(data.status==true)
     {  
        
        paginaC(); //funzione chiamata per visualizzare il nuovo inserimento
      

     }
     else
     {
        alert('Errore inserimento ');
     }
 
 }
 
 
 } 

 function getStudent() //visualizzare la tabella dei studenti di una determinata classe
 {
    var xhr=new XMLHttpRequest();
    idClasse=idputC;
  
    xhr.open("GET",'http://localhost:8080/School_Servlet/M_Student?idClass='+reload,true);//passato un id che è la classe per prendere solo quei alunni
    xhr.send();
    xhr.onload=function()
    { 
        var data =JSON.parse(xhr.response);
     
        $('#insert').attr("data-target","#MStudente"); //cambio del target 
        $('#insert').attr("id","insertS");
        $('#thead').empty();
        $('#titleT').html("Elenco Alunni");
        $('#thead').append('<tr><th style="display:none;" class="text-center">id</th> <th class="text-center">Cognome</th> <th class="text-center">Nome</th>'+
        '<th class="text-center">sidi Code</th> <th class="text-center">Tax Code</th>'+
        '<th class="text-center" style="width: 100px;">Modifica </th>'+
        '<th class="text-center" style="width: 100px;">Elimina </th> </tr>')
    
        for(var i = 0; i <data.studentInfo.length; i++)
        { 
            
            
                $('#thead').append('<tr><td style="display:none;" class=pt-3-half id="idD">'+data.studentInfo[i].id +'</td><td class=pt-3-half>'+ data.studentInfo[i].surname+'</td>'+'<td class=pt-3-half>'+ data.studentInfo[i].name+'</td>'+
                '</td><td class=pt-3-half>'+ data.studentInfo[i].sidiCode+'</td>'+'<td class=pt-3-half>'+ data.studentInfo[i].taxCode+'</td>'+
                '<td><span class="table-modifica" >  <button  data-toggle="modal" data-target="#MStudente" style="border-radius: 10px;;color:green" id="mStudente"><span class="glyphicon glyphicon-edit"></span></button></span> </td>'+
                '<td><span class="table-removeS" id=elimina > <button type="button" style="border-radius: 10px;;color:red" id=elimina><span class="glyphicon glyphicon-trash"></span></button></span> </td></tr>');
    }
}

    xhr.onerror=function()
    {
    alert("errore");
    }

   

 }

 function modificaS()//modifica student
 {
    var xhr=new XMLHttpRequest();
    xhr.open("PUT",'http://localhost:8080/School_Servlet/M_Student',true);
    xhr.send(JSON.stringify({
        "id":idputC,
        "name":document.getElementById("Mnome").value,
        "surname":document.getElementById("Mcognome").value,
        "sidiCode":document.getElementById("MSidiCode").value,
        "taxCode":document.getElementById("MTaxCode").value,
        }));
     

xhr.onload=function()
    {
    
    var data =JSON.parse(xhr.response);
    if(data.status==true)
    {   
        getStudent();//per visualizzare le modifiche
        
       
    }
    else
    {
        alert("errore");
    }
    }
}
function deleteS(classId,thi)//elimina student
{
    var domanda = confirm("Sei sicuro di voler cancellare la classe?");//window conferma
    if (domanda === true)
    {
    var id=classId;
    var xhr=new XMLHttpRequest();
    xhr.open("DELETE",'http://localhost:8080/School_Servlet/M_Student?idStudent='+id,true);
    xhr.send();   
xhr.onload=function()
{
    
    var data =JSON.parse(xhr.response);
    
    if(data.status==true)
    {   
        
        $(thi).parents('tr').detach(); //passato $(this) dalla classe deleteS per capire cosa eliminare
       
    }
    else if(data.status==false)
    {
       alert("errore eliminazione");
    }

}
    }
        xhr.onerror=function()
        {
        alert("errore");
        }
}

function insertS()//inserimento Student
{
    var xhr=new XMLHttpRequest();
    xhr.open("POST",'http://localhost:8080/School_Servlet/M_Student',true);
    xhr.send(JSON.stringify({
        "name":document.getElementById("Mnome").value,
        "surname":document.getElementById("Mcognome").value,
        "sidiCode":document.getElementById("MSidiCode").value,
        "taxCode":document.getElementById("MTaxCode").value,
        }));
     

xhr.onload=function()
    {
    
    var data =JSON.parse(xhr.response);
    
        idStudent=data.studentInfo; //prendere id del studente per inserirlo nella tabella class_student
       student_class(idStudent);//inserisce id studente e id class nella tab class_student
   

}

}


function student_class(idStudent)//inserimento idstudent e idclass
{ 
    var xhr=new XMLHttpRequest();
    xhr.open("POST",'http://localhost:8080/School_Servlet/M_Class_Student',true);
    xhr.send(JSON.stringify({
        "idStudent":idStudent,
        "idClass":idClasse
        }));

        xhr.onload=function()
    {
    
    var data =JSON.parse(xhr.response);
    if(data.status==true)
    {   
        alert('studente inserito nella classe');
       getStudent();  //per visualizzare le modifiche e l'inserimento del nuovo student
    }
    else
    {
        alert("errore");
    }

}
}