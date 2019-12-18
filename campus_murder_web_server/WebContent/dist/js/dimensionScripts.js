var box_dimensions = 4;

function increaseDimension(){
	if(box_dimensions == 4){
		document.getElementById("first_column").className = "col-lg-6 col-md-6 col-sm-6  connectedSortable";
		document.getElementById("second_column").className = "col-lg-6 col-md-6 col-sm-6 connectedSortable";
		document.getElementById("third_column").className = "col-lg-6 col-md-6 col-sm-6 connectedSortable";
		document.getElementById("fourth_column").className = "col-lg-6 col-md-6 col-sm-6 connectedSortable";
		
		box_dimensions = 6;
	}
	else{
		if(box_dimensions == 6){
			document.getElementById("first_column").className = "col-lg-12 col-md-12 col-sm-12 connectedSortable";
			document.getElementById("second_column").className = "col-lg-12 col-md-12 col-sm-12 connectedSortable";
			document.getElementById("third_column").className = "col-lg-12 col-md-12 col-sm-12 connectedSortable";
			document.getElementById("fourth_column").className = "col-lg-12 col-md-12 col-sm-12 connectedSortable";
		
			box_dimensions = 12
		}
	}

}


function decreaseDimension(){
	if(box_dimensions == 12){
		document.getElementById("first_column").className = "col-lg-6 col-md-6 col-sm-6 connectedSortable";
		document.getElementById("second_column").className = "col-lg-6 col-md-6 col-sm-6 connectedSortable";
		document.getElementById("third_column").className = "col-lg-6 col-md-6 col-sm-6 connectedSortable";
		document.getElementById("fourth_column").className = "col-lg-6 col-md-6 col-sm-6 connectedSortable";
		box_dimensions = 6;
	}
	else{
		if(box_dimensions == 6){
			document.getElementById("first_column").className = "col-lg-3 col-md-3 col-sm-6 connectedSortable";
			document.getElementById("second_column").className = "col-lg-3 col-md-3 col-sm-6 connectedSortable";
			document.getElementById("third_column").className = "col-lg-3 col-md-3 col-sm-6 connectedSortable";
			document.getElementById("fourth_column").className = "col-lg-3 col-md-3 col-sm-6 connectedSortable";
			box_dimensions = 4
		}
	}

}

