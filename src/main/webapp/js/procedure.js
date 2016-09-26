
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
		
		$element.change(function(event) {
			var data = $JQry(this).select2('data');
			$form = $JQry(this).closest("form");
			$form.find("input[name$='newField.label']").val(data[0].label);
			$form.find("select[name$='newField.type']").val(data[0].type);
			$form.find("input[name$='newField.varOptions']").val(data[0].varOptions);		
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
});

function selectPath(button, name) {
	var path =$JQry(button).parents("li").find("input[name$='path']").val();
	selector(button, path, name);
}

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
		
		if (variable.varOptions != null) {
			$optionsrow = $JQry(document.createElement("div")).addClass("row");
			$optionslabel = $JQry(document.createElement("label")).addClass("col-sm-3");
			$optionslabel.text("Options");
			$optionslabel.appendTo($optionsrow);
			$optionsdiv = $JQry(document.createElement("div")).addClass("col-sm-9");
			$optionsdiv.text(variable.varOptions);
			$optionsdiv.appendTo($optionsrow);
			$optionsrow.appendTo($result);
		}
	}
	return $result;
};
