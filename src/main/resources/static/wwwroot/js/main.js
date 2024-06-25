$(document).ready(function () {
        var countrySelect = $('#country');

        // Gọi API để lấy danh sách quốc gia
        $.ajax({
            url: 'https://countriesnow.space/api/v0.1/countries',
            method: 'GET',
            success: function (data) {
                console.log('Thành công: ', data);

                // Đổ dữ liệu vào dropdown
                data.data.forEach(function (item) {
                    countrySelect.append($('<option>', {
                        value: item.country,
                        text: item.country
                    }));
                });
            },
            error: function (xhr, status, error) {
                console.error('Lỗi khi lấy dữ liệu quốc gia:', error);
                // Xử lý lỗi nếu cần thiết (ví dụ: hiển thị thông báo cho người dùng)
            }
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