document.addEventListener('DOMContentLoaded', function() {
    var calendarEl = document.getElementById('calendar');
    var calendar = new FullCalendar.Calendar(calendarEl, {
        initialView: 'dayGridMonth',
        events: studies.map(function(study) {
            return {
                title: study.name,
                start: study.startTime,
                end: study.endTime
            };
        })
    });
    calendar.render();
});
