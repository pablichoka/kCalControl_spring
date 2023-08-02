//Ajax for buttons
function ajaxB(element){
    event.preventDefault();
  let spinner = document.getElementById('mainSpinner');
  var url = element.getAttribute('href');
  var xhr = new XMLHttpRequest();

  xhr.open('GET', url, true);
  document.getElementById('container').innerHTML = "";
  spinner.style.display = 'flex';

  xhr.onload = function() {
      document.getElementById('container').innerHTML = xhr.responseText;
      spinner.style.display = 'none';
    }
  xhr.send();   
};

//Ajax for functions
function ajaxF(url){
  // event.preventDefault();
  let spinner = document.getElementById('mainSpinner');
  var xhr = new XMLHttpRequest();
  xhr.open('GET', url, true);
  document.getElementById('container').innerHTML = "";
  spinner.style.display = 'flex';
  xhr.onload = function() {
    document.getElementById('container').innerHTML = xhr.responseText;
    spinner.style.display = 'none';
  };
  xhr.send();
}

//Ajax for search
function ajaxForm(formName, id) {
  let spinner = document.getElementById('mainSpinner');
  let form = document.getElementById(formName);
  
  if (form) {
    spinner.style.display = 'flex';
    var url = form.getAttribute('action');
    var csrfToken = document.getElementById('csrf').value;
    var formData = new FormData(form);
    var xhr = new XMLHttpRequest();
    xhr.open('POST', url, true);
    xhr.setRequestHeader('X-CSRF-TOKEN', csrfToken);
    document.getElementById(id).innerHTML = "";

    xhr.onload = function() {
      var parser = new DOMParser();
    var doc = parser.parseFromString(xhr.responseText, 'text/html');
    var div = doc.getElementById(id);
      document.getElementById(id).innerHTML = div.innerHTML;
      spinner.style.display = 'none';
    }
    
    xhr.send(formData);
  }
}

function ajaxBMData(url){
  // event.preventDefault();
  let spinner = document.getElementById('mainSpinner');
  var xhr = new XMLHttpRequest();
  xhr.open('GET', url, true);
  document.getElementById('containerBMData').innerHTML = "";
  spinner.style.display = 'flex';
  xhr.onload = function() {
    var parser = new DOMParser();
    var doc = parser.parseFromString(xhr.responseText, 'text/html');
    var div = doc.getElementById('containerBMData');
    document.getElementById('containerBMData').innerHTML = div.innerHTML;
    spinner.style.display = 'none';
  };
  xhr.send();
}

function ajaxBMCalc(url){
  // event.preventDefault();
  let spinner = document.getElementById('mainSpinner');
  var xhr = new XMLHttpRequest();
  xhr.open('GET', url, true);
  document.getElementById('containerBMCalc').innerHTML = "";
  spinner.style.display = 'flex';
  xhr.onload = function() {
    var parser = new DOMParser();
    var doc = parser.parseFromString(xhr.responseText, 'text/html');
    var div = doc.getElementById('containerBMCalc');
    document.getElementById('containerBMCalc').innerHTML = div.innerHTML;
    spinner.style.display = 'none';
  };
  xhr.send();
}

let isProcessing = false;

function ajaxIngCat(url, formName) {
  if (isProcessing) {
    return;
  }
  
  let spinner = document.getElementById('spinner');
  var xhr = new XMLHttpRequest();
  xhr.open('POST', url, true);
  spinner.style.display = 'inherit';
  var csrfToken = document.getElementById('csrf').value;
  xhr.setRequestHeader('X-CSRF-TOKEN', csrfToken);
  let formData = new FormData(document.getElementById(formName));
  xhr.onload = function() {
    if (xhr.status === 204) {
      spinner.style.display = 'none';
      addErrorMessage('Type correctly added!', 'success');
    } else if (xhr.status === 409) {
      spinner.style.display = 'none';
      addErrorMessage('Type is already added for that category!', 'warning');
    } else {
      spinner.style.display = 'none';
      addErrorMessage('Something went wrong!', 'danger');
    }
    isProcessing = false;
  };
  isProcessing = true;
  xhr.send(formData);
}
