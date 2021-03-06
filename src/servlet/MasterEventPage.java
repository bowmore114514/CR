
package servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.BordItems2;
import dao.Event2;
import dao.EventDAO;
import model.CreateAndAnnihilateEventPage;
import model.Event;

/**
 * Servlet implementation class MasterEventPage
 */
@WebServlet("/MasterEventPage")
public class MasterEventPage extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public MasterEventPage() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=" + "UTF-8");
		response.getWriter().append("Served at: ").append(request.getContextPath());

		//URLの取得
		/*String url = new String(request.getRequestURL());

		String eventIdS = Event.getIdFromURL(url);

		int eventId = Integer.parseInt(eventIdS);*/

		//リクエストパラメータでpageIdを取得 (pageId=eventId)
		String eventIdS = Event.getIdFromURL(request.getParameter("pageid"));


		//String から int に直す
		int eventId = Integer.parseInt(eventIdS);


		//Event2インスタンス
		//適当な初期値
		String str = "str";
		int Int = 0;
		Calendar cal = Calendar.getInstance();

		Event2 event0 = new Event2(str, str, str, cal, str,
									str, cal, cal, Int, Int, str, str, str);

		//DAO
		EventDAO dao = new EventDAO();

		ArrayList<Event2> event2 = dao.getEvent2List();

		for (Event2 event:event2){
			if (Integer.parseInt(event.getEventId()) == Integer.parseInt(eventIdS)){

				event0 = event;

			}
		}

		//event0からそれぞれの値を取得
		String eventName = event0.getEventName();
		String organizarName = event0.getOrganizarName();
		Calendar registDay = event0.getRegistDay();
		String autherName = event0.getAutherName();
		String autherPass = event0.getAutherPass();
		Calendar deadlineDay = event0.getDeadlineDay();
		Calendar determinedDay = event0.getDeterminedDay();
		int determinedFlag = event0.getDeterminedFlag();
		int eventOpenFlag = event0.getEventOpenFlag();
		String numberOfEvent = event0.getNumberOfEvent();
		String eventUrl = event0.getEventUrl();
		String eventPageFileName = event0.getEventPageFileName();


		//各DBテーブルからeventIdに応じた値をすべて取得
		ArrayList<String> eventVenue = dao.getEventVenueList(eventId);
		ArrayList<String> autherRemark = dao.getAutherRemarkList(eventId);
		ArrayList<String> pricePerPerson = dao.getPricePerPersonList(eventId);
		ArrayList<Calendar> candidate = dao.getCandidateList(eventId);


		//インスタンス生成
		Event event = new Event(eventName, organizarName, eventVenue,
				registDay, autherName, autherPass, deadlineDay,
				autherRemark, determinedDay, determinedFlag, eventOpenFlag,
				numberOfEvent, eventUrl, eventPageFileName,pricePerPerson,
				candidate);
		event.setEventId(String.valueOf(eventId));
		CreateAndAnnihilateEventPage.createEventPageUrl(event);




		//BordItems2
		ArrayList<BordItems2> bordItems2 = dao.getBordItemList(eventId);
		ArrayList<ArrayList<Integer>> preferredFlagSet = new ArrayList<ArrayList<Integer>>();

		for (BordItems2 item : bordItems2){
			preferredFlagSet.add(dao.getPreferredFlagSet(eventId,item.getItemId()));
		}


		//セッションスコープに保存
		HttpSession session = request.getSession();
		session.setAttribute("event",event);
		session.setAttribute("bordItems2", bordItems2);
		session.setAttribute("preferredFlagSet", preferredFlagSet);



		//フォワード
		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/masterEdit.jsp");
		dispatcher.forward(request, response);

	}
}

