  document.addEventListener('DOMContentLoaded', function() {
          var calendarEl = document.getElementById('calendar');
          var calendar;

          if (calendarEl) {
              var storedEvents = localStorage.getItem('calendarEvents');
              var initialEvents = storedEvents ? JSON.parse(storedEvents) : [];

              calendar = new FullCalendar.Calendar(calendarEl, {
                  initialView: 'dayGridMonth',
                  editable: true,
                  selectable: true,
                  events: initialEvents,
                  dateClick: function(info) {
                      $('#doctorModal').modal('show');
                      $('#saveDoctorBtn').data('selectedDate', info.dateStr);
                  },
                  eventClick: function(info) {
                      var event = info.event;
                        var doctorNameToSelect = event.title;
                        console.log(doctorNameToSelect);

                        $('#updateDoctorSelect option').each(function() {
                            if ($(this).text().toLowerCase() === doctorNameToSelect.toLowerCase()) {
                                $(this).prop('selected', true);
                            }
                        });
                      $('#updateStartTimeInput').val(event.extendedProps.startTime);
                      $('#updateEndTimeInput').val(event.extendedProps.endTime);

                      $('#updateEventBtn').data('eventId', event.id);

                      $('#updateDeleteEventModal').modal('show');
                  }
              });

              calendar.render();
          } else {
              console.error('Calendar element not found. Make sure the element with id "calendar" exists in the HTML.');
          }

          $('#saveDoctorBtn').click(function() {
              var doctorId = $('#doctorSelect').val();
              var startTime = $('#startTimeInput').val();
              var endTime = $('#endTimeInput').val();
              var date = $(this).data('selectedDate');

              $.ajax({
                  url: '/api/horaires',
                  type: 'POST',
                  data: {
                      doctorId: doctorId,
                      startTime: startTime,
                      endTime: endTime,
                      date: date
                  },
                  success: function(response) {
                      var newEvent = {
                          id: response.eventId,
                          title: response.doctorName,
                          start: date,
                          extendedProps: {
                              startTime: response.startTime,
                              endTime: response.endTime,
                              doctorId:response.doctorId,
                              eventType: 'appointment'
                          },
                          classNames: ['appointment']
                      };

                      calendar.addEvent(newEvent);
                      var events = calendar.getEvents();
                      localStorage.setItem('calendarEvents', JSON.stringify(events));

                      $('#doctorModal').modal('hide');
                  },
                  error: function(error) {
                      console.error('Error saving schedule:', error);
                  }
              });
          });

          $('#deleteEventBtn').click(function() {
           $('#updateDeleteEventModal').modal('hide');
           $('#confirmDeleteModal').modal('show');
          });

          $('#confirmDeleteBtn').click(function() {
              var eventId = $('#updateEventBtn').data('eventId');
              $('#confirmDeleteModal').modal('hide'); // Cacher la modal de confirmation

              $.ajax({
                  url: '/api/deleteEvent',
                  type: 'POST',
                  data: {
                      eventId: eventId
                  },
                  success: function(response) {
                      var event = calendar.getEventById(eventId);
                      if (event) {
                          event.remove();
                          var events = calendar.getEvents();
                          localStorage.setItem('calendarEvents', JSON.stringify(events));
                      } else {
                          console.error('Event not found with ID:', eventId);
                      }
                  },
                  error: function(error) {
                      console.error('Error deleting event:', error);
                  }
              });
          });
       $('#updateEventBtn').click(function() {
    var eventId = $(this).data('eventId');
    var doctorName = $('#updateDoctorSelect option:selected').text();
    var doctorId = $('#updateDoctorSelect option:selected').val();
    var startTime = $('#updateStartTimeInput').val();
    var endTime = $('#updateEndTimeInput').val();

    // Récupérer la date de l'événement à partir du calendrier
    var eventToUpdate = calendar.getEventById(eventId);
    var eventDate = eventToUpdate.startStr;
    // Construire un objet avec les données à mettre à jour
    var updatedEventData = {
        eventId: eventId,
        doctorName: doctorName,
        startTime: startTime,
        endTime: endTime,
        date: eventDate,
        doctorId:doctorId
    };

    // Envoyer la requête POST pour mettre à jour l'événement
    $.ajax({
        url: '/api/updateEvent',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(updatedEventData),
       success: function(response) {
    // Retrieve the event from the calendar by ID
    var eventToUpdate = calendar.getEventById(eventId);

    if (eventToUpdate) {
        // Update the event's properties
        eventToUpdate.setProp('title', response.doctorName);
        eventToUpdate.setProp('classNames', ['appointment']);
        eventToUpdate.setExtendedProp('startTime', response.startTime);
        eventToUpdate.setExtendedProp('endTime', response.endTime);
        var events = calendar.getEvents();
        localStorage.setItem('calendarEvents', JSON.stringify(events));
        // Update the event in the calendar
        eventToUpdate.remove(); // Remove the event (necessary for FullCalendar v5+)
        calendar.addEvent(eventToUpdate); // Add the updated event back to the calendar

        // Close the modal for updating the event
        $('#updateDeleteEventModal').modal('hide');
    } else {
        console.error('Event not found with ID:', eventId);
    }
},
        error: function(error) {
            console.error('Error updating event:', error);
        }
    });
});
      });
