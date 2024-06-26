$(document).ready(function () {
        var countrySelect = $('#country');

        $.ajax({
            url: 'https://countriesnow.space/api/v0.1/countries',
            method: 'GET',
            success: function (data) {
                data.data.forEach(function (item) {
                    countrySelect.append($('<option>', {
                        value: item.country,
                        text: item.country
                    }));
                });
            },

        });

        var artistSelect = $('#artistSelect');
            var addArtistButton = $('#addArtistButton');
            var selectedArtists = $('#selectedArtists');
            var submitButton = $('#submit');
            var hiddenInput = $('#selectedNgheSiList');

            addArtistButton.on('click', function (event) {
                event.preventDefault();
                var selectedOption = artistSelect.find('option:selected');
                var existingOption = selectedArtists.find(`option[value="${selectedOption.val()}"]`);
                if (selectedOption.val() !== "" && existingOption.length === 0) {
                    var newOption = $('<option>', {
                        value: selectedOption.val(),
                        text: selectedOption.text()
                    });
                    selectedArtists.append(newOption);
                }
            });

            submitButton.on('click', function (event) {
                var selectedValues = selectedArtists.find('option').map(function () {
                    return $(this).val();
                }).get();

                hiddenInput.val(selectedValues.join(','));

            });
});