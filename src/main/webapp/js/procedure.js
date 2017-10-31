
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
		e.stopImmediatePropagation();
	});
	// on sauvegarde la position de départ de l'item
	ui.item.data("itemStartPosition",ui.item.position());
	ui.item.data("moving", true);
};

function sortableStop(filter, event, ui){
	// empêche la sélection du champ ou filtre
	$JQry(event.originalEvent.target).off();
	$JQry(event.originalEvent.target).on('click',function(e){
		e.stopImmediatePropagation();
		event.preventDefault(); // cas control-label
	});
	var itemPosition = ui.item.position();
	var itemStartPosition = ui.item.data('itemStartPosition');
	// on ne maj le form que si la position a changée
	if(itemPosition.top != itemStartPosition.top || itemPosition.left != itemStartPosition.left){
		$JQry(filter).children("li").each(function(index, element){
			updatePath(index, [], element);
		});
		// empêche de lancer l'évènement click sur le parent
		event.stopPropagation();
		event.preventDefault(); // cas control-label
		
		// soumet la mise à jour du formulaire
		$JQry(ui.item).closest("form").find("input[name='updateForm']").click();
	}else{
		setTimeout(function() {
			ui.item.data("moving", false);
		},300);
	}
};

function sortableTableHelper (e, tr) {
	var $originals = tr.children();
    var $helper = tr.clone();
    $helper.children().each(function(index) {
    	$JQry(this).width($originals.eq(index).width());
    });
    return $helper;
};

function updateFilters(input){
	 var filter = input.value.toUpperCase();
	 $JQry(".filterSelect-results").children(".panel").each(function(index, element) {
		 $element = $JQry(element);
		 var h3 = $element.find("h3")[0];
		 var body = $element.children(".panel-body")[0];
		 
		 if(h3.innerHTML.toUpperCase().indexOf(filter) > -1 || body.innerHTML.toUpperCase().indexOf(filter) > -1){
			 element.style.display = "";
		 }else{
			 element.style.display = "none";
		 }
	 });
};

function insertVarValueAtCaret(varElement) {
	
	var varValue = $JQry(varElement).children(".col-sm-4").first().text();
	
	if($JQry(".insertatcaretactive").length !== 0){
		$JQry(".insertatcaretactive").each(function(i) {
			insertValueAtCaret(this, varValue);
		});
	}else{
		var varElement = $JQry(".filter-argument").first();
		insertValueAtCaret(varElement, varValue);
	}
};

function insertValueAtCaret(varElement, varValue){
	if (document.selection) {
        //Internet Explorer
		varElement.focus();
        sel = document.selection.createRange();
        sel.text = varValue;
        varElement.focus();
    } else if (varElement.selectionStart || varElement.selectionStart == '0') {
        //Firefox and Webkit based
        var startPos = varElement.selectionStart;
        var endPos = varElement.selectionEnd;
        var scrollTop = varElement.scrollTop;
        varElement.value = varElement.value.substring(0, startPos) + varValue + varElement.value.substring(endPos, varElement.value.length);
        varElement.focus();
        varElement.selectionStart = startPos + varValue.length;
        varElement.selectionEnd = startPos + varValue.length;
        varElement.scrollTop = scrollTop;
    } else {
        varElement[0].value += varValue;
        varElement.focus();
    }
};

$JQry(function() {
	$JQry(".dates-selector").datepicker({
		changeMonth : true,
		changeYear : true,
		dateFormat : "dd/mm/yy",
		numberOfMonths : 1,
		selectOtherMonths : true,

		beforeShow : function(input, inst) {
			customizeRendering(inst);
		},
		
		onChangeMonthYear : function(year, month, inst) {
			customizeRendering(inst);
		},
		
		onClose : function(dateText, inst) {
			var fromSuffix = "-date-from";
			var toSuffix = "-date-to";
			
			if (this.id.indexOf(fromSuffix, this.id.length - fromSuffix.length) !== -1) {
				// From
				var prefix = this.id.substring(0, this.id.length - fromSuffix.length);
				var $to = $JQry("#" + prefix + toSuffix);
				$to.datepicker("option", "minDate", dateText);
			} else if (this.id.indexOf(toSuffix, this.id.length - toSuffix.length) !== -1) {
				// To
				var prefix = this.id.substring(0, this.id.length - toSuffix.length);
				var $from = $JQry("#" + prefix + fromSuffix);
				$from.datepicker("option", "maxDate", dateText);
			}
		}
	});
	
	$JQry("#procedure-sortable ul").sortable({
		connectWith : "#procedure-sortable ul",
		cursor : "move",
		tolerance : "pointer",
		axis : "y",
		forcePlaceholderSize: true,
		placeholder: "procedure-sortable-placeHolder",
		start :function(event, ui) {
			sortableStart(event, ui);
		},
		update: function(event, ui) {
			sortableStop("#procedure-sortable > ul", event, ui);
		}
	});
	
	$JQry(".steps-sortable").sortable({
		cursor : "move",
		tolerance : "pointer",
		axis: "y",
		stop: function(event, ui) {
			$JQry(this).find("li").each(function(index, element) {
				$JQry(element).find("input[name$='index']").val(index);
			});
		}
	});
	
	$JQry(".column-sortable").sortable({
		cursor : "move",
		tolerance : "pointer",
		axis: "y",
		items: " .procedure-column",
		stop: function(event, ui) {
			$JQry(this).find(".procedure-column").each(function(index, element) {
				$JQry(element).find("input[name$='index']").val(index);
			});
			// soumet la mise à jour du formulaire
			$JQry(ui.item).closest("form").find("input[name='updateDashboard']").click();
		}
	});
	
	$JQry(".exportVar-sortable").sortable({
		cursor : "move",
		tolerance : "pointer",
		axis: "y",
		items: " .procedure-export",
		stop: function(event, ui) {
			var exportVarList = [];
			$JQry(this).find(".procedure-export").each(function(index, element) {
				var exportVar = $JQry(element).find("td").first().text();
				exportVarList.push(exportVar);
			});
			
			selector(this,exportVarList,'exportVarList');
			// soumet la mise à jour du formulaire
			$JQry(ui.item).closest("form").find("input[name='updateDashboard']").click();
		}
	});
	
	// sélection d'un champ
	$JQry("#procedure-sortable li").click(function(event) {
		
		var $target = $JQry(this);
		if(!$target.hasClass("select2-selection") && !$target.hasClass("ui-sortable-helper") &&  !$target.data("moving")){
			// make selected
			$JQry(".fieldSelected").removeClass("fieldSelected");
			$JQry("#Edit").find("input").attr("disabled", "true");
			$target.closest("li").addClass("fieldSelected");
			// find hidden input by end name
			var path = $JQry(this).children("input[name$='path']").val();
			// add value to form as selected
			selector(this,path,'selectedFieldPath');
			// click hidden selectFilter button
			$JQry(this).closest('form').find("input[name='selectField']").click();
		}
		
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
		update: function(event, ui) {
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
	
	// make collpase button active when collapsable is open
	$JQry(".btn[data-toggle='collapse']").click(function(e){
		var $btn = $JQry(this);
		if($btn.hasClass('active')){
			$btn.removeClass('active');
		}else{
			$btn.addClass('active');
		}
	});
	
	$JQry(".filter-argument").on("focus", function() {
		$JQry(".insertatcaretactive").removeClass("insertatcaretactive");
		$JQry(this).addClass("insertatcaretactive");
	});
	
	$JQry(".procedure-variables > .row").draggable({
		helper: function() {
			var varValue = $JQry(this).children(".col-sm-4").first().text();
	        return $JQry("<div></div>").text(varValue).addClass("procedure-variables-placeHolder");
	    },
	    scroll : false,
	    zIndex : 100,
	    cursorAt: { top: 5, left: 5 },
		appendTo: "body"
	});
	$JQry(".filter-argument").droppable({
	    accept: ".procedure-variables > .row",
	    hoverClass: "filter-argument-dropActive",
	    tolerance : "pointer",
	    drop: function(ev, ui) {
	    	var varValue = ui.draggable.children(".col-sm-4").first().text();
	    	var varElement = $(this);
	    	insertValueAtCaret(varElement, varValue);
	    }
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
		var defaultVars = $element.data("defaultvars");
		var autofill = $element.data("autofill");
		
		$element.select2({
			ajax: {
				url: $element.data("url"),
				dataType: 'json',
				delay: 300,
				data: function (params) {
					return {
						filter: params.term,
						defaultVars: defaultVars
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
		
		if (autofill) {
			// maj des champs avec la sélection
			$element
					.change(function(event) {
						var data = $JQry(this).select2('data');
						$form = $JQry(this).closest("form");
						$form.find("input[name$='newField.label']").val(
								data[0].label);

						var $newCOlumn = $form
								.find("input[name$='newColumn.label']");
						// si le champ n'est pas déjà remplis
						if ($newCOlumn.length && $newCOlumn.val().length == 0) {
							$newCOlumn.val(data[0].label);
						}

						if (data[0].type != null) {
							$form.find("select[name$='newField.type']").val(
									data[0].type.id);
						}
						$form.find("input[name$='newField.varOptions']").val(
								data[0].varOptions);
						// maj de l'éditeur avec le type
						$JQry("select[name$='newField.type']").each(
								updateNewFieldType);
					});
		}
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
			$JQry("#formulaire-newField-additional-options").removeClass("hidden");
			$JQry("input[name$='newField.helpText']").closest("div.form-group").removeClass("hidden");
			$JQry("input[name$='newField.varOptions']").closest("div.form-group").addClass("hidden");
			
			// maj du tableau des options
			var json =  $JQry("input[name$='newField.varOptions']").val();
			if (json.trim().length > 0) {
				var rowList = JSON.parse(json);
				for (var i = 0; i < rowList.length; i++) {
					$JQry("#formulaire-newField-list-editor-optionList").find("tbody").append(buildRow(rowList[i].label, rowList[i].value));
				}
			}
			$JQry("#formulaire-newField-list-editor-optionList tbody").sortable({
				helper: sortableTableHelper,
				forcePlaceholderSize: true,
				forceHelperSize: true,
				axis : "y",
				start :function(event, ui) {
					ui.helper.display = "table";
				},
				stop: function(event, ui) {
					$JQry("input[name$='newField.varOptions']").val(jsonifyList($JQry("#formulaire-newField-list-editor-optionList").find("tbody")));
				}
			});
			$JQry("#formulaire-newField-list-editor").removeClass("hidden");
		}else if($JQry(this).val() == 'TEXT'){
			$JQry("#formulaire-newField-list-editor").addClass("hidden");
			$JQry("#formulaire-newField-additional-options").removeClass("hidden");
			$JQry("input[name$='newField.helpText']").closest("div.form-group").removeClass("hidden");
			$JQry("input[name$='newField.varOptions']").closest("div.form-group").addClass("hidden");
		}else if($JQry(this).val() == 'TEXTAREA'){
			$JQry("#formulaire-newField-list-editor").addClass("hidden");
			$JQry("#formulaire-newField-additional-options").removeClass("hidden");
			$JQry("input[name$='newField.helpText']").closest("div.form-group").removeClass("hidden");
			$JQry("input[name$='newField.varOptions']").closest("div.form-group").addClass("hidden");
		}else if($JQry(this).val() == 'DISPLAY'){
			$JQry("#formulaire-newField-list-editor").addClass("hidden");
			$JQry("#formulaire-newField-additional-options").addClass("hidden");
			$JQry("input[name$='newField.helpText']").closest("div.form-group").addClass("hidden");
			$JQry("input[name$='newField.varOptions']").closest("div.form-group").removeClass("hidden");
		}else{
			$JQry("#formulaire-newField-list-editor").addClass("hidden");
			$JQry("#formulaire-newField-additional-options").removeClass("hidden");
			$JQry("input[name$='newField.helpText']").closest("div.form-group").removeClass("hidden");
			$JQry("input[name$='newField.varOptions']").closest("div.form-group").addClass("hidden");
		}
	};
	$JQry("select[name$='newField.type']").change(updateNewFieldType);
	$JQry("select[name$='newField.type']").each(updateNewFieldType);
	
	// gestion du type de champ sélectionné
	var updateSelectedFieldType = function() {
		if($JQry(this).val() == 'RADIOLIST' || $JQry(this).val() == 'CHECKBOXLIST' || $JQry(this).val() == 'SELECTLIST'){
			$JQry("#formulaire-selectedField-additional-options").removeClass("hidden");
			$JQry("input[name$='selectedField.helpText']").closest("div.form-group").removeClass("hidden");
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
			$JQry("#formulaire-selectedField-list-editor-optionList tbody").sortable({
				helper: sortableTableHelper,
				forcePlaceholderSize: true,
				forceHelperSize: true,
				axis : "y",
				start :function(event, ui) {
					ui.helper.display = "table";
				},
				stop: function(event, ui) {
					$JQry("input[name$='selectedField.varOptions']").val(jsonifyList($JQry("#formulaire-selectedField-list-editor-optionList").find("tbody")));
				}
			});
			$JQry("#formulaire-selectedField-list-editor").removeClass("hidden");
		}else if($JQry(this).val() == 'TEXT'){
			$JQry("#formulaire-selectedField-list-editor").addClass("hidden");
			$JQry("#formulaire-selectedField-additional-options").removeClass("hidden");
			$JQry("input[name$='selectedField.helpText']").closest("div.form-group").removeClass("hidden");
			$JQry("input[name$='selectedField.varOptions']").closest("div.form-group").addClass("hidden");
		}else if($JQry(this).val() == 'TEXTAREA'){
			$JQry("#formulaire-selectedField-list-editor").addClass("hidden");
			$JQry("#formulaire-selectedField-additional-options").removeClass("hidden");
			$JQry("input[name$='selectedField.helpText']").closest("div.form-group").removeClass("hidden");
			$JQry("input[name$='selectedField.varOptions']").closest("div.form-group").addClass("hidden");
		}else if($JQry(this).val() == 'DISPLAY'){
			$JQry("#formulaire-selectedField-list-editor").addClass("hidden");
			$JQry("#formulaire-selectedField-additional-options").addClass("hidden");
			$JQry("input[name$='selectedField.helpText']").closest("div.form-group").addClass("hidden");
			$JQry("input[name$='selectedField.varOptions']").closest("div.form-group").removeClass("hidden");
		}else{
			$JQry("#formulaire-selectedField-list-editor").addClass("hidden");
			$JQry("#formulaire-selectedField-additional-options").removeClass("hidden");
			$JQry("input[name$='selectedField.helpText']").closest("div.form-group").removeClass("hidden");
			$JQry("input[name$='selectedField.varOptions']").closest("div.form-group").addClass("hidden");
		}
	};
	
	var removeRow = function(){
		$JQry(this).closest("tr").remove();
		// on maj le json du champ option
		$JQry("input[name$='newField.varOptions']").val(jsonifyList($JQry("#formulaire-newField-list-editor-optionList").find("tbody")));
		$JQry("input[name$='selectedField.varOptions']").val(jsonifyList($JQry("#formulaire-selectedField-list-editor-optionList").find("tbody")));
	};
	
	// éditeur bouton radio - ajout
	$JQry("#formulaire-newField-list-editor-addOption").click(function() {
		//ajout d'une option dans la liste
		var label = $JQry("#formulaire-newField-list-editor-newOption-label").val().replace(/'/g, '&apos;');
		var value = $JQry("#formulaire-newField-list-editor-newOption-value").val().replace(/'/g, '&apos;');
		$JQry("#formulaire-newField-list-editor-optionList").find("tbody").append(buildRow(label, value));
		
		// on maj le json du champ option
		$JQry("input[name$='newField.varOptions']").val(jsonifyList($JQry("#formulaire-newField-list-editor-optionList").find("tbody")));
		
		$JQry("#formulaire-newField-list-editor-newOption-label").val("");
		$JQry("#formulaire-newField-list-editor-newOption-value").val("");
		
		$JQry(".formulaire-list-editor-removeOption").click(removeRow);
	});
	
	// construction des champs radio à partir des données
	$JQry(".field-radioList-json").each(function(index){
		var name = $JQry(this).attr("name");
		var dataValue = $JQry(this).val();
		var json = $JQry(this).data("varoptions");
		if (json.length > 0) {
			var radioList = json.reverse();
			for (var i = 0; i < radioList.length; i++) {
				$JQry(this).after(makeRadioFromData(name, radioList[i].label, radioList[i].value, dataValue));
			}
		}
	});
	$JQry(".field-radioList-json").each(function(index, element){
		element.remove();
	});
	
	// construction des champs checkbox à partir des données
	$JQry(".field-checkboxList-json").each(function(index){
		var name = $JQry(this).attr("name");
		var dataValue = $JQry(this).val();
		var json = $JQry(this).data("varoptions");
		if (json.length > 0) {
			var checkboxList = json.reverse();
			for (var i = 0; i < checkboxList.length; i++) {
				$JQry(this).after(makeCheckboxFromData(name, checkboxList[i].label, checkboxList[i].value, dataValue));
			}
		}
	});
	$JQry(".field-checkboxList-json").each(function(index, element){
		element.remove();
	});
	
	// construction des champs select à partir des données
	$JQry(".field-selectList-json").each(function(index){
		var name = $JQry(this).attr("name");
		var dataValue = $JQry(this).val();
		var json = $JQry(this).data("varoptions");
		if (json.length > 0) {
			var selectList = json;
			makeSelectFromData(this, name, selectList, dataValue);
		}
	});
	$JQry(".field-selectList-json").each(function(index, element){
		element.remove();
	});
	
	$JQry("input[name$='selectedField.type']").each(updateSelectedFieldType);
	
	// éditeur radio/select - edit
	$JQry("#formulaire-selectedField-list-editor-addOption").click(function() {
		//ajout d'une option dans la liste
		var label = $JQry("#formulaire-selectedField-list-editor-newOption-label").val().replace(/'/g, '&apos;');
		var value = $JQry("#formulaire-selectedField-list-editor-newOption-value").val().replace(/'/g, '&apos;');
		$JQry("#formulaire-selectedField-list-editor-optionList").find("tbody").append(buildRow(label, value));
		
		// on maj le json du champ option
		$JQry("input[name$='selectedField.varOptions']").val(jsonifyList($JQry("#formulaire-selectedField-list-editor-optionList").find("tbody")));
		
		$JQry("#formulaire-selectedField-list-editor-newOption-label").val("");
		$JQry("#formulaire-selectedField-list-editor-newOption-value").val("");
		
		$JQry(".formulaire-list-editor-removeOption").click(removeRow);
	});
	
	$JQry(".formulaire-list-editor-removeOption").click(removeRow);
});

function makeRadioFromData(name, label, value, dataValue){
	var inputTag = document.createElement("input");
	$JQry(inputTag).attr({
		type : 'radio',
		name : name,
		value : value
	});
	var labelTag = document.createElement("label");
	
	if(value==dataValue){
		$JQry(inputTag).prop("checked", true);
	}
	
	return $JQry(labelTag).addClass("radio-inline").append(inputTag).append(label);
}

function makeCheckboxFromData(name, label, value, dataValue){
	var inputTag = document.createElement("input");
	$JQry(inputTag).attr({
		type : 'checkbox',
		name : name,
		value : value
	});
	
	if(value==dataValue){
		$JQry(inputTag).prop("checked", true);
	}
	
	var labelTag = document.createElement("label");
	return $JQry(labelTag).addClass("checkbox-inline").append(inputTag).append(label);
}

function makeSelectFromData(element, name, selectList, dataValue){
	var selectTag = document.createElement("select");
	
	for (var i = 0; i < selectList.length; i++) {
		var optionTag = document.createElement("option");
		$JQry(selectTag).append($JQry(optionTag).val(selectList[i].value).text(selectList[i].label));
	}
	$JQry(element).after(selectTag);
	$JQry(selectTag).val(dataValue);
	$JQry(selectTag).addClass("form-control").attr("name",name).select2({
		theme : "bootstrap"
	});
}

function jsonifyList(tbody){
	var list = [];
	$JQry(tbody).children("tr").each(function( index ){
		var td = $JQry(this).children("td");
		var label = $JQry(td[0]).text().replace(/'/g, '&apos;');
		var value = $JQry(td[1]).text().replace(/'/g, '&apos;');
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
	return group.text;
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
			$typediv.text(variable.type.label);
			$typediv.appendTo($typerow);
			$typerow.appendTo($result);
		}
	}
	return $result;
};

function customizeRendering(inst) {
	setTimeout(function() {
		// z-index
		inst.dpDiv.css("z-index", 10);
		
		// Header
		var $header = inst.dpDiv.find(".ui-datepicker-header");
		
		// Previous button
		var $previous = $header.find(".ui-datepicker-prev");
		$previous.addClass("btn btn-default pull-left");
		$previous.find("span").remove();
		$previous.append($JQry(document.createElement("i")).addClass("halflings halflings-circle-arrow-left"));
		
		// Next button
		var $next = $header.find(".ui-datepicker-next");
		$next.addClass("btn btn-default pull-right");
		$next.find("span").remove();
		$next.append($JQry(document.createElement("i")).addClass("halflings halflings-circle-arrow-right"));
		
		// Form
		var $title = $header.find(".ui-datepicker-title");
		$title.addClass("form-inline text-overflow");
		$title.children("select").addClass("form-control");
		$title.children("span").addClass("form-control-static");
	}, 0);
} 
