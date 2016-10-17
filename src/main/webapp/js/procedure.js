
function updatePath(index, currentpath, element) {
	var elementPath = currentpath.slice();
	elementPath.push(index);
	if(!$JQry(element).find("input[name$='canary']").length){
		$JQry(element).find("input[name$='path'],input[name$='filterPath']").val(elementPath);
		$JQry('<input>').attr({
			type : 'hidden',
			name : 'canary',
			value : 'true'
		}).appendTo(element);
	}
	
	var nestedPath = elementPath.slice();
	if($JQry(element).find("ul").length){
		$JQry(element).find("ul").children("li").each(function(loopIndex,element){
			updatePath(loopIndex, nestedPath, element);
		});
	}
};

function sortableStart(event, ui){
	// empêche la sélection du champ ou filtre
	$JQry(event.originalEvent.target).off();
	$JQry(event.originalEvent.target).on('click',function(e){
		e.preventDefault(); // cas control-label
		e.stopImmediatePropagation();
	});
	// on sauvegarde la position de départ de l'item
	ui.item.data("itemStartPosition",ui.item.position());
};

function sortableStop(filter, event, ui){
	var itemPosition = ui.item.position();
	var itemStartPosition = ui.item.data('itemStartPosition');
	// on ne maj le form que si la position a changée
	if(itemPosition.top != itemStartPosition.top || itemPosition.left != itemStartPosition.left){
		$JQry(filter).children("li").each(function(index, element){
			updatePath(index, [], element);
		});
		// soumet la mise à jour du formulaire
		$JQry(ui.item).closest("form").find("input[name='updateForm']").click();
	}
	e.preventDefault(); // cas control-label
	e.stopImmediatePropagation();
}

$JQry(function() {
	$JQry("#procedure-sortable ul").sortable({
		connectWith : "#procedure-sortable ul",
		cursor : "move",
		tolerance : "pointer",
		axis: "y",
		forcePlaceholderSize: true,
		placeholder: "procedure-sortable-placeHolder",
		start :function(event, ui) {
			sortableStart(event, ui);
		},
		stop: function(event, ui) {
			sortableStop("#procedure-sortable > ul", event, ui);
		}
	});
	
	// sélection d'un champ
	$JQry("#procedure-sortable li").click(function(event) {
		// find hidden input by end name
		var path = $JQry(this).children("input[name$='path']").val();
		// add value to form as selected
		selector(this,path,'selectedFieldPath');
		// click hidden selectFilter button
		$JQry(this).closest('form').find("input[name='selectField']").click();
		// empêche de lancer l'évènement click sur le parent
		event.stopPropagation();
		event.preventDefault(); // cas control-label
	});
	
	$JQry("#filter-sortable ul").sortable({
		connectWith : "#filter-sortable ul",
		cursor : "move",
		tolerance : "pointer",
		axis: "y",
		forcePlaceholderSize: true,
		placeholder: "filter-sortable-placeHolder",
		start : function(event, ui) {
			sortableStart(event, ui);
		},
		stop: function(event, ui) {
			sortableStop("#filter-sortable > ul", event, ui);
		}
	});
	
	// sélection d'un filtre
	$JQry("#filter-sortable li").click(function(event) {
		// find hidden input by end name
		var path = $JQry(this).children("input[name$='filterPath']").val();
		// add value to form as selected
		selector(this,path,'selectedFilterPath');
		// click hidden selectFilter button
		$JQry(this).closest('form').find("input[name='selectFilter']").click();
		// empêche de lancer l'évènement click sur le parent
		event.stopPropagation();
	});
	
	// nested bootstrap tabs
	$JQry("ul.nav-tabs a").click(function (e) {
		e.preventDefault();  
		$JQry(this).tab('show');
	});
	
	$JQry(".vocabularySelect-select2").each(function(index, element) {
		var $element = $JQry(element);
		var vocabularySearchUrl = $element.data("url");
		
		$element.select2({
			ajax: {
				url: $element.data("url"),
				dataType: 'json',
				delay: 300,
				data: function (params) {
					return {
						filter: params.term
					};
				},
				processResults: function (data, params) {
					return {
						results: data
					};
				},
				cache: true
			},
			escapeMarkup: function (markup) { return markup; },
			theme: "bootstrap",
			templateResult: formatProfil,
			templateSelection: formatProfil
		});
		
	});
	
	$JQry(".fieldSelect-select2").each(function(index, element) {
		var $element = $JQry(element);
		var vocabularySearchUrl = $element.data("url");
		
		$element.select2({
			ajax: {
				url: $element.data("url"),
				dataType: 'json',
				delay: 300,
				data: function (params) {
					return {
						filter: params.term
					};
				},
				processResults: function (data, params) {
					return {
			          results: $JQry.map(data, function(variable) {
			        	  variable.id = variable.name;
			        	  variable.text = variable.name;
			        	  return variable;
			          })
			        };
				},
				cache: true
			},
			escapeMarkup: function (markup) { return markup; },
			theme: "bootstrap",
			templateResult: formatField
		});
		
		// maj des champs avec la sélection
		$element.change(function(event) {
			var data = $JQry(this).select2('data');
			$form = $JQry(this).closest("form");
			$form.find("input[name$='newField.label']").val(data[0].label);
			$form.find("select[name$='newField.type']").val(data[0].type);
			$form.find("input[name$='newField.varOptions']").val(data[0].varOptions);
			// maj de l'éditeur avec le type
			$JQry("select[name$='newField.type']").each(updateNewFieldType);
		});
	});
	
	$JQry(".groupSelect-select2").each(function(index, element) {
		var $element = $JQry(element);
		var groupSearchUrl = $element.data("url");
		
		$element.select2({
			ajax: {
				url: groupSearchUrl,
				dataType: 'json',
				delay: 300,
				data: function (params) {
					return {
						filter: params.term
					};
				},
				processResults: function (data, params) {
					return {
						results: $JQry.map(data, function(group) {
							return { id: group.cn, text: group.displayName };
						})
					};
				},
				cache: true
			},
			escapeMarkup: function (markup) { return markup; },
			minimumInputLength: 3,
			theme: "bootstrap",
			templateResult: formatProfil,
			templateSelection: formatProfil
		});
	});
	
	$JQry(".stepSelect-select2").each(function(index, element) {
		var $element = $JQry(element);
		var stepSearchUrl = $element.data("url");
		
		$element.select2({
			ajax: {
				url: stepSearchUrl,
				dataType: 'json',
				delay: 300,
				data: function (params) {
					return {
						filter: params.term
					};
				},
				processResults: function (data, params) {
					return {
						results: data
					};
				},
				cache: true
			},
			escapeMarkup: function (markup) { return markup; },
			theme: "bootstrap",
			templateResult: formatProfil,
			templateSelection: formatProfil
		});
	});
	
	// gestion du type de nouveau champ
	var updateNewFieldType = function() {
		if($JQry(this).val() == 'RADIOLIST' || $JQry(this).val() == 'CHECKBOXLIST' || $JQry(this).val() == 'SELECTLIST'){
			$JQry("input[name$='newField.varOptions']").closest("div.form-group").addClass("hidden");
			
			// maj du tableau des options
			var json =  $JQry("input[name$='newField.varOptions']").val();
			if (json.trim().length > 0) {
				var rowList = JSON.parse(json);
				for (var i = 0; i < rowList.length; i++) {
					$JQry("#formulaire-newField-list-editor-optionList").find("tbody").append(buildRow(rowList[i].label, rowList[i].value));
				}
			}
			
			$JQry("#formulaire-newField-list-editor").removeClass("hidden");
		}else if($JQry(this).val() == 'TEXT'){
			$JQry("#formulaire-newField-list-editor").addClass("hidden");
			$JQry("input[name$='newField.varOptions']").closest("div.form-group").addClass("hidden");
		}else if($JQry(this).val() == 'TEXTAREA'){
			$JQry("#formulaire-newField-list-editor").addClass("hidden");
			$JQry("input[name$='newField.varOptions']").closest("div.form-group").addClass("hidden");
		}else{
			$JQry("#formulaire-newField-list-editor").addClass("hidden");
			$JQry("input[name$='newField.varOptions']").closest("div.form-group").addClass("hidden");
		}
	};
	$JQry("select[name$='newField.type']").change(updateNewFieldType);
	$JQry("select[name$='newField.type']").each(updateNewFieldType);
	
	var removeRow = function(){
		$JQry(this).closest("tr").remove();
		// on maj le json du champ option
		$JQry("input[name$='newField.varOptions']").val(jsonifyList($JQry("#formulaire-newField-list-editor-optionList").find("tbody")));
		$JQry("input[name$='selectedField.varOptions']").val(jsonifyList($JQry("#formulaire-selectedField-list-editor-optionList").find("tbody")));
	};
	
	// éditeur bouton radio - ajout
	$JQry("#formulaire-newField-list-editor-addOption").click(function() {
		//ajout d'une option dans la liste
		var label = $JQry("#formulaire-newField-list-editor-newOption-label").val();
		var value = $JQry("#formulaire-newField-list-editor-newOption-value").val();
		$JQry("#formulaire-newField-list-editor-optionList").find("tbody").append(buildRow(label, value));
		
		// on maj le json du champ option
		$JQry("input[name$='newField.varOptions']").val(jsonifyList($JQry("#formulaire-newField-list-editor-optionList").find("tbody")));
		
		$JQry(".formulaire-list-editor-removeOption").click(removeRow);
	});
	
	// construction des champs radio à partir des données
	$JQry(".field-radioList-json").each(function(index){
		var name = $JQry(this).attr("name");
		var json = $JQry(this).val();
		if (json.trim().length > 0) {
			var radioList = JSON.parse(json).reverse();
			for (var i = 0; i < radioList.length; i++) {
				$JQry(this).after(makeRadioFromData(name, radioList[i].label, radioList[i].value));
			}
		}
	});
	// construction des champs checkbox à partir des données
	$JQry(".field-checkboxList-json").each(function(index){
		var name = $JQry(this).attr("name");
		var json = $JQry(this).val();
		if (json.trim().length > 0) {
			var checkboxList = JSON.parse(json).reverse();
			for (var i = 0; i < checkboxList.length; i++) {
				$JQry(this).after(makeCheckboxFromData(name, checkboxList[i].label, checkboxList[i].value));
			}
		}
	});
	// construction des champs select à partir des données
	$JQry(".field-selectList-json").each(function(index){
		var name = $JQry(this).attr("name");
		var json = $JQry(this).val();
		if (json.trim().length > 0) {
			var selectList = JSON.parse(json);
			makeSelectFromData(this, name, selectList);
		}
	});
	
	// gestion du type de champ édité
	var updateSelectedFieldType = function() {
		if($JQry(this).val() == 'RADIOLIST'|| $JQry(this).val() == 'CHECKBOXLIST' || $JQry(this).val() == 'SELECTLIST'){
			$JQry("input[name$='selectedField.varOptions']").closest("div.form-group").addClass("hidden");
			
			// maj du tableau des options
			$JQry("#formulaire-selectedField-list-editor-optionList").find("tbody").empty();
			var json =  $JQry("input[name$='selectedField.varOptions']").val();
			if (json.trim().length > 0) {
				var rowList = JSON.parse(json);
				for (var i = 0; i < rowList.length; i++) {
					$JQry("#formulaire-selectedField-list-editor-optionList").find("tbody").append(buildRow(rowList[i].label, rowList[i].value));
				}
			}
			
			$JQry("#formulaire-selectedField-list-editor").removeClass("hidden");
		}else if($JQry(this).val() == 'TEXT'){
			$JQry("#formulaire-selectedField-list-editor").addClass("hidden");
			$JQry("input[name$='selectedField.varOptions']").closest("div.form-group").addClass("hidden");
		}else if($JQry(this).val() == 'TEXTAREA'){
			$JQry("#formulaire-selectedField-list-editor").addClass("hidden");
			$JQry("input[name$='selectedField.varOptions']").closest("div.form-group").addClass("hidden");
		}else{
			$JQry("#formulaire-selectedField-list-editor").addClass("hidden");
			$JQry("input[name$='selectedField.varOptions']").closest("div.form-group").removeClass("hidden");
		}
	};
	$JQry("select[name$='selectedField.type']").change(updateSelectedFieldType);
	$JQry("select[name$='selectedField.type']").each(updateSelectedFieldType);
	
	// éditeur radio/select - edit
	$JQry("#formulaire-selectedField-list-editor-addOption").click(function() {
		//ajout d'une option dans la liste
		var label = $JQry("#formulaire-selectedField-list-editor-newOption-label").val();
		var value = $JQry("#formulaire-selectedField-list-editor-newOption-value").val();
		$JQry("#formulaire-selectedField-list-editor-optionList").find("tbody").append(buildRow(label, value));
		
		// on maj le json du champ option
		$JQry("input[name$='selectedField.varOptions']").val(jsonifyList($JQry("#formulaire-selectedField-list-editor-optionList").find("tbody")));
		
		$JQry(".formulaire-list-editor-removeOption").click(removeRow);
	});
	
	$JQry(".formulaire-list-editor-removeOption").click(removeRow);
	
});

function makeRadioFromData(name, label, value){
	var inputTag = document.createElement("input");
	$JQry(inputTag).attr({
		type : 'radio',
		name : name,
		value : value
	});
	var labelTag = document.createElement("label");
	return $JQry(labelTag).addClass("radio-inline").append(inputTag).append(label);
}

function makeCheckboxFromData(name, label, value){
	var inputTag = document.createElement("input");
	$JQry(inputTag).attr({
		type : 'checkbox',
		name : name,
		value : value
	});
	var labelTag = document.createElement("label");
	return $JQry(labelTag).addClass("checkbox-inline").append(inputTag).append(label);
}

function makeSelectFromData(element, name, selectList){
	var selectTag = document.createElement("select");
	
	for (var i = 0; i < selectList.length; i++) {
		var optionTag = document.createElement("option");
		$JQry(selectTag).append($JQry(optionTag).val(selectList[i].value).text(selectList[i].label));
	}
	$JQry(element).after(selectTag);
	
	$JQry(selectTag).addClass("form-control").attr("name",name).select2({
		theme : "bootstrap"
	});
}

function jsonifyList(tbody){
	var list = [];
	$JQry(tbody).children("tr").each(function( index ){
		var td = $JQry(this).children("td");
		var label = $JQry(td[0]).text();
		var value = $JQry(td[1]).text();
		var radio = {label:label,value:value};
		list.push(radio);
	});
	return JSON.stringify(list);
}

function buildRow(label, value){
	var row = document.createElement("tr");
	var tdLabel = document.createElement("td");
	var tdValue = document.createElement("td");
	var tdDel = document.createElement("td");
	$JQry(tdLabel).append(label);
	$JQry(tdValue).append(value);
	
	var radioDelButton = document.createElement("button");
	var radioDelGlyph = document.createElement("i");
	$JQry(radioDelGlyph).addClass("glyphicons glyphicons-bin");
	$JQry(radioDelButton).addClass("btn btn-default formulaire-list-editor-removeOption").attr("type","button");
	$JQry(radioDelButton).append(radioDelGlyph);
	$JQry(tdDel).append(radioDelButton);
	
	return $JQry(row).append(tdLabel).append(tdValue).append(tdDel);
};

function selectPath(button, name) {
	var path =$JQry(button).parents("li").find("input[name$='path']").val();
	selector(button, path, name);
};

function selector(button, value, name) {
	var form = $JQry(button).closest('form');
	$JQry('<input>').attr({
		type : 'hidden',
		name : name,
		value : value
	}).appendTo(form);
};

function formatProfil (group) {
	return group.text + ' (' + group.id + ')';
};

function formatField(variable) {
	$result = $JQry(document.createElement("div"));
	
	if (variable.loading) {
		$result.text(variable.text);
	} else {
		$result.addClass("variable-select2");
		
		$namerow = $JQry(document.createElement("div")).addClass("row");
		$namelabel = $JQry(document.createElement("label")).addClass("col-sm-3");
		$namelabel.text("Nom");
		$namelabel.appendTo($namerow);
		$namediv = $JQry(document.createElement("div")).addClass("col-sm-9");
		$namediv.text(variable.name);
		$namediv.appendTo($namerow);
		$namerow.appendTo($result);
		
		if (variable.label != null) {
			$labelrow = $JQry(document.createElement("div")).addClass("row");
			$labellabel = $JQry(document.createElement("label")).addClass(
					"col-sm-3");
			$labellabel.text("Label");
			$labellabel.appendTo($labelrow);
			$labeldiv = $JQry(document.createElement("div")).addClass(
					"col-sm-9");
			$labeldiv.text(variable.label);
			$labeldiv.appendTo($labelrow);
			$labelrow.appendTo($result);
		}
		
		if (variable.type != null) {
			$typerow = $JQry(document.createElement("div")).addClass("row");
			$typelabel = $JQry(document.createElement("label")).addClass("col-sm-3");
			$typelabel.text("Type");
			$typelabel.appendTo($typerow);
			$typediv = $JQry(document.createElement("div")).addClass("col-sm-9");
			$typediv.text(variable.type);
			$typediv.appendTo($typerow);
			$typerow.appendTo($result);
		}
	}
	return $result;
};
