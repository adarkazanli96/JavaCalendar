import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class CalendarController {
	private CalendarView view;
	private CalendarModel model;
	private GregorianCalendar cal;
	private File eventFile;
	private File inputFile;

	public CalendarController(CalendarModel model, CalendarView view) {
		this.model = model;
		this.view = view;
		cal = model.getCal();

		this.view.addTodayButton(new TodayListener());
		this.view.addNextButton(new NextListener());
		this.view.addPrevButton(new PrevListener());
		this.view.addDayButton(new DayListener());
		this.view.addWeekButton(new WeekListener());
		this.view.addMonthButton(new MonthListener());
		this.view.addAgendaButton(new AgendaListener());
		this.view.addCreateButton(new CreateListener());
		this.view.addDayEventButton(new DayEventListener());
		this.view.addRecurringEventButton(new RecurringEventListener());

		this.view.addOptionWindowCancelButton(new OptionWindowCancel());

		this.view.addDayEventWindowSaveButton(new DayEventWindowSave());
		this.view.addDayEventWindowCancelButton(new DayEventWindowCancel());

		this.view.addRecurringEventWindowSaveButton(new RecurringEventWindowSave());
		this.view.addRecurringEventWindowCancelButton(new RecurringEventWindowCancel());
	}

	public class TodayListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			model.resetCalendar();

			cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
			view.repaint();
			view.printDayEventsText();
		}
	}

	public class NextListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (view.getViewBy().equals("Day")) {
				// set the day to one day before and repaint
				cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH) + 1);
				model.updateCalendar(cal);
				view.repaint();
				view.printDayEventsText();

			}

			else if (view.getViewBy().equals("Week")) {
				// set to previous week and repaint
				cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH) + 7);
				view.updateCalendar(cal);
				model.updateCalendar(cal);
				view.repaint();
				view.printWeekEventsText();

			}

			else if (view.getViewBy().equals("Month")) {
				// set to previous month and repaint
				cal.set(cal.get(Calendar.YEAR), (cal.get(Calendar.MONTH)) + 1, 1);
				view.updateCalendar(cal);
				model.updateCalendar(cal);
				view.repaint();
				view.printMonthEventsText();
			}
		}
	}

	public class PrevListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (view.getViewBy().equals("Day")) {
				// set the day to one day before and repaint
				cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH) - 1);
				model.updateCalendar(cal);
				view.repaint();
				view.printDayEventsText();
			}

			else if (view.getViewBy().equals("Week")) {
				// set to previous week and repaint
				cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH) - 7);
				view.updateCalendar(cal);
				model.updateCalendar(cal);
				view.repaint();
				view.printWeekEventsText();

			} else if (view.getViewBy().equals("Month")) {
				// set to previous month and repaint
				cal.set(cal.get(Calendar.YEAR), (cal.get(Calendar.MONTH)) - 1, 1);
				view.updateCalendar(cal);
				model.updateCalendar(cal);
				view.repaint();
				view.printMonthEventsText();
			}

		}

	}

	public class DayListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			view.setViewBy("Day");

			view.dayButton.setForeground(Color.BLUE);
			view.weekButton.setForeground(Color.BLACK);
			view.monthButton.setForeground(Color.BLACK);

			// print the events for that day
			view.printDayEventsText();
		}
	}

	public class WeekListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			view.setViewBy("Week");

			view.dayButton.setForeground(Color.BLACK);
			view.weekButton.setForeground(Color.BLUE);
			view.monthButton.setForeground(Color.BLACK);

			// print the events for that week
			view.printWeekEventsText();
		}
	}

	public class MonthListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			view.setViewBy("Month");

			view.dayButton.setForeground(Color.BLACK);
			view.weekButton.setForeground(Color.BLACK);
			view.monthButton.setForeground(Color.BLUE);

			// print events for that month
			view.printMonthEventsText();
		}
	}

	public class AgendaListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			// print all the events
			view.printAgenda();
		}
	}

	public class CreateListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			view.createEvent();
		}
	}

	public class DayEventListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			view.createDayEvent();
			view.deleteOptionWindow();
		}

	}

	public class RecurringEventListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			view.createRecurringEvent();
			view.deleteOptionWindow();
		}

	}

	public class OptionWindowCancel implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			view.deleteOptionWindow();
		}

	}

	public class DayEventWindowCancel implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			view.deleteDayEventWindow();
		}

	}

	public class DayEventWindowSave implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			System.out.println("Entered save listener");

			DayEvent temp = null;

			for (DayEvent event : model.getAllDayEvents()) {

				/*
				 * System.out.println(event.getYear()+" "+cal.get(Calendar.YEAR) );
				 * System.out.println(event.getMonth()+" "+((cal.get(Calendar.MONTH))+1));
				 * System.out.println(event.getDay()+" "+ cal.get(Calendar.DAY_OF_MONTH));
				 * System.out.println(event.getStartingTime()+" "+
				 * Integer.parseInt(view.getStartTimeTextField().getText()));
				 */

				if ((event.getYear() == cal.get(Calendar.YEAR) && event.getMonth() == (cal.get(Calendar.MONTH)) + 1
						&& event.getDay() == cal.get(Calendar.DAY_OF_MONTH)
						&& Integer.parseInt(view.getStartTimeTextField().getText()) >= event.getStartingTime()
						&& Integer.parseInt(view.getStartTimeTextField().getText()) < event.getEndingTime())
						|| 
						(event.getYear() == cal.get(Calendar.YEAR) && event.getMonth() == (cal.get(Calendar.MONTH)) + 1
						&& event.getDay() == cal.get(Calendar.DAY_OF_MONTH)
						&&Integer.parseInt(view.getEndTimeTextField().getText()) < event.getEndingTime()
								&& Integer.parseInt(view.getEndTimeTextField().getText()) > event.getStartingTime())) {

					// System.out.println("Entered conflic");

					view.displayErrorMessage("Time Conflict! Try Again");
				}

				else {

					// System.out.println("Entered else");

					temp = new DayEvent(view.getEventTextField().getText(), cal.get(Calendar.YEAR),
							(cal.get(Calendar.MONTH)) + 1, cal.get(Calendar.DAY_OF_MONTH),
							Integer.parseInt(view.getStartTimeTextField().getText()),
							Integer.parseInt(view.getEndTimeTextField().getText()));

				}

			}

			model.addDayEvent(temp);

			model.sortByDateTime();

			eventFile = new File("events.txt");

			try {
				eventFile.createNewFile();
			} catch (IOException e1) {

				e1.printStackTrace();
			}

			FileWriter fw = null;

			try {

				fw = new FileWriter(eventFile);
			}

			catch (IOException e1) {

				e1.printStackTrace();
			}

			PrintWriter pw = new PrintWriter(fw);

			for (DayEvent event : model.getAllDayEvents()) {

				// System.out.println(event);

				pw.write(event.getEventName() + ";" + event.getYear() + ";" + event.getMonth() + ";" + event.getDay()
						+ ";" + event.getStartingTime() + ";" + event.getEndingTime() + "\n");
			}

			pw.close();

			if (view.getViewBy().equals("Day")) {
				view.repaint();
				view.printDayEventsText();
			}

			else if (view.getViewBy().equals("Week")) {
				view.repaint();
				view.printWeekEventsText();

			} else if (view.getViewBy().equals("Month")) {
				view.repaint();
				view.printMonthEventsText();
			}

			view.deleteDayEventWindow();
		}

	}

	public class RecurringEventWindowSave implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			
			String date[] = view.getRecurringEventStartDateField().getText().split("/");
			int year = Integer.parseInt(date[2]);
			int startingMonth = Integer.parseInt(date[0]);
			
			//System.out.println(year+" "+startingMonth);

			String date2[] = view.getRecurringEventEndDateField().getText().split("/");
			int endingMonth = Integer.parseInt(date2[0]);

			RecurringEvent temp = new RecurringEvent(view.getRecurringEventEventField().getText(), year, startingMonth,
					endingMonth, view.getRecurringEventDayField().getText(),
					Integer.parseInt(view.getRecurringEventStartTimeField().getText()),
					Integer.parseInt(view.getRecurringEventEndTimeField().getText()));

			ArrayList<RecurringEvent> recurringList = new ArrayList<RecurringEvent>();
			recurringList.add(temp);
			ArrayList<DayEvent> dayList = model.convert(recurringList);
			
			//System.out.println(recurringList.size());
			//System.out.println(dayList.size());

			boolean conflict = false;
			
			outerloop: 
				for (DayEvent d1 : model.getAllDayEvents()) {
					
					for (DayEvent d2 : dayList) {
						
						
						
						if (d1.getYear() == d2.getYear()
								&& d1.getMonth() == d2.getMonth()
								&& d1.getDay() == d2.getDay()
								&& d2.getStartingTime() >= d1.getStartingTime()
								&& d2.getStartingTime() < d1.getEndingTime()
								
								|| 
								
								d1.getYear() == d2.getYear()
								&& d1.getMonth() == d2.getMonth()
								&& d1.getDay() == d2.getDay()
								&& d2.getEndingTime() >= d1.getEndingTime()
								&& d2.getEndingTime() < d1.getStartingTime()

								) {
							
							System.out.println("entered conflict");
							
							System.out.println(d1.getDay());
							System.out.println(d2.getDay()+"\n");
							
							view.displayErrorMessage("Time Conflict! Try Again");
							
							
							
							conflict = true;
							
							break outerloop;
						}
					}

				}
			
				if (conflict == false) {
					
					model.dayEventsList.addAll(dayList);
					model.recurringEventsList.addAll(recurringList);
					model.sortByDateTime();
					view.deleteRecurringEventWindow();
				}
				
				model.sortByDateTime();

				inputFile = new File("input.txt");

				try {
					inputFile.createNewFile();
				} catch (IOException e1) {

					e1.printStackTrace();
				}

				FileWriter fww = null;

				try {

					fww = new FileWriter(inputFile);
				}

				catch (IOException e1) {

					e1.printStackTrace();
				}

				PrintWriter pww = new PrintWriter(fww);

				for (RecurringEvent event : model.getAllRecurringEvents()) {

					// System.out.println(event);

					pww.write(event.getEventName() + ";" + event.getYear() + ";" + event.getStartingMonth() + ";" + event.getEndingMonth()
							+";"+ event.getDays() +";" + event.getStartingTime() + ";" + event.getEndingTime() + "\n");
				}

				pww.close();
				
				eventFile = new File("events.txt");

				try {
					eventFile.createNewFile();
				} catch (IOException e1) {

					e1.printStackTrace();
				}

				FileWriter fw = null;

				try {

					fw = new FileWriter(eventFile);
				}

				catch (IOException e1) {

					e1.printStackTrace();
				}

				PrintWriter pw = new PrintWriter(fw);

				for (DayEvent event : model.getAllDayEvents()) {

					// System.out.println(event);

					pw.write(event.getEventName() + ";" + event.getYear() + ";" + event.getMonth() + ";" + event.getDay()
							+ ";" + event.getStartingTime() + ";" + event.getEndingTime() + "\n");
				}

				pw.close();
				
			if (view.getViewBy().equals("Day")) {
				view.repaint();
				view.printDayEventsText();
			}

			else if (view.getViewBy().equals("Week")) {
				view.repaint();
				view.printWeekEventsText();

			} else if (view.getViewBy().equals("Month")) {
				view.repaint();
				view.printMonthEventsText();
			}
		}

	}

	public class RecurringEventWindowCancel implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			view.deleteRecurringEventWindow();
		}
	}
}
