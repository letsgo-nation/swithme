document.addEventListener('DOMContentLoaded', function () {
    var modeSwitch = document.querySelector('.mode-switch');

    modeSwitch.addEventListener('click', function () {
        document.documentElement.classList.toggle('dark');
        modeSwitch.classList.toggle('active');
    });

    // $.ajax({
    //     type: 'GET',
    //     url: '/api/categories',
    //     success: function(response) {
    //         let form_category = $('#category');
    //         form_category.empty();
    //         let data = response['data'];
    //         for(let i=0;i<data.length;i++) {
    //             let category = data[i]
    //             let category_id = category['id'];
    //             let name = category['name'];
    //             let option_value = i+1;
    //             form_category.append(`<option value=${option_value}>${name}</option>`);
    //         }
    //     }
    // })




});

