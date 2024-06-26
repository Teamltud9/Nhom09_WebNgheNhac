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

        $("#search-input").on("keyup", function () {
            console.log("Key pressed:", event.key);
            var query = $(this).val();
            if (query !== '') {
                $.ajax({
                    url: "/SearchSuggestions",
                    type: "GET",
                    data: { query: query },
                    dataType: "json",
                    success: function (data) {
                        $("#search-results").empty();
                        $.each(data, function (index, suggestion) {
                            $("#search-results").append("<div class='suggestion'>" + suggestion + "</div>");
                        });
                        $(".dropdown-content").show();
                    },
                    error: function (xhr, status, error) {
                        console.log("Lỗi:", error);
                    }
                });
            } else {
                $("#search-results").empty();
                $(".dropdown-content").hide();
            }
        });
        $(document).on("click", ".suggestion", function () {
            var selectedSuggestion = $(this).text();
            $("#search-input").val(selectedSuggestion);
            $("#search-results").empty();
            $(".dropdown-content").hide();
        });

        $(document).on("click", function (event) {
            if (!$(event.target).closest('.dropdown').length) {
                $(".dropdown-content").hide();
            }
        });
});