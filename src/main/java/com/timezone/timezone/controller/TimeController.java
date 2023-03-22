package com.timezone.timezone.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.timezone.timezone.domain.ApiResponse;
import com.timezone.timezone.domain.TimeZoneResponse;

@RestController
@RequestMapping("/time")
public class TimeController {

	private static final String HTTP_WORLDTIMEAPI_ORG_API_TIMEZONE = "http://worldtimeapi.org/api/timezone/";
	private static final String HTTP_WORLDTIMEAPI_ORG_API_TIMEZONE_AMERICA = "http://worldtimeapi.org/api/timezone/America";

	private static List<String> timezoneList = null;

	@Autowired
	private RestTemplate restTemplate;

	@GetMapping
	public ResponseEntity<ApiResponse> getUSTime(@RequestParam("timezone") String timezone)
			throws JsonProcessingException {
		if (isValidTimeZoneForUSA(timezone)) {
			return ResponseEntity.ok(getByTimeZone(timezone));
		}
		return ResponseEntity.badRequest().build();
	}

	public boolean isValidTimeZoneForUSA(final String timezone) {
		if (timezoneList == null) {
			timezoneList = Arrays
					.asList(restTemplate.getForObject(HTTP_WORLDTIMEAPI_ORG_API_TIMEZONE_AMERICA, String[].class));
		}
		return timezoneList.stream().filter(tz -> timezone.equals(tz)).findFirst().isPresent();
	}

	public ApiResponse getByTimeZone(@RequestParam final String timezone) throws JsonProcessingException {
		String url = HTTP_WORLDTIMEAPI_ORG_API_TIMEZONE + timezone;
		TimeZoneResponse currentTime = restTemplate.getForObject(url, TimeZoneResponse.class);
		ApiResponse time = new ApiResponse();
		time.setAbbreviation(currentTime.getAbbreviation());
		time.setTimezone(currentTime.getDatetime());
		time.setUtc_datetime(currentTime.getTimezone());
		return time;
	}
	
	@ExceptionHandler(value =Throwable.class)
	public ResponseEntity<String> handleAllExceptions(Throwable t){
		
		return new ResponseEntity<String>(t.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
		
	}
//[America/Adak, America/Anchorage, America/Araguaina, America/Argentina/Buenos_Aires, America/Argentina/Catamarca, America/Argentina/Cordoba, America/Argentina/Jujuy, America/Argentina/La_Rioja, America/Argentina/Mendoza, America/Argentina/Rio_Gallegos, America/Argentina/Salta, America/Argentina/San_Juan, America/Argentina/San_Luis, America/Argentina/Tucuman, America/Argentina/Ushuaia, America/Asuncion, America/Atikokan, America/Bahia, America/Bahia_Banderas, America/Barbados, America/Belem, America/Belize, America/Blanc-Sablon, America/Boa_Vista, America/Bogota, America/Boise, America/Cambridge_Bay, America/Campo_Grande, America/Cancun, America/Caracas, America/Cayenne, America/Chicago, America/Chihuahua, America/Costa_Rica, America/Creston, America/Cuiaba, America/Curacao, America/Danmarkshavn, America/Dawson, America/Dawson_Creek, America/Denver, America/Detroit, America/Edmonton, America/Eirunepe, America/El_Salvador, America/Fort_Nelson, America/Fortaleza, America/Glace_Bay, America/Goose_Bay, America/Grand_Turk, America/Guatemala, America/Guayaquil, America/Guyana, America/Halifax, America/Havana, America/Hermosillo, America/Indiana/Indianapolis, America/Indiana/Knox, America/Indiana/Marengo, America/Indiana/Petersburg, America/Indiana/Tell_City, America/Indiana/Vevay, America/Indiana/Vincennes, America/Indiana/Winamac, America/Inuvik, America/Iqaluit, America/Jamaica, America/Juneau, America/Kentucky/Louisville, America/Kentucky/Monticello, America/La_Paz, America/Lima, America/Los_Angeles, America/Maceio, America/Managua, America/Manaus, America/Martinique, America/Matamoros, America/Mazatlan, America/Menominee, America/Merida, America/Metlakatla, America/Mexico_City, America/Miquelon, America/Moncton, America/Monterrey, America/Montevideo, America/Nassau, America/New_York, America/Nipigon, America/Nome, America/Noronha, America/North_Dakota/Beulah, America/North_Dakota/Center, America/North_Dakota/New_Salem, America/Nuuk, America/Ojinaga, America/Panama, America/Pangnirtung, America/Paramaribo, America/Phoenix, America/Port-au-Prince, America/Port_of_Spain, America/Porto_Velho, America/Puerto_Rico, America/Punta_Arenas, America/Rainy_River, America/Rankin_Inlet, America/Recife, America/Regina, America/Resolute, America/Rio_Branco, America/Santarem, America/Santiago, America/Santo_Domingo, America/Sao_Paulo, America/Scoresbysund, America/Sitka, America/St_Johns, America/Swift_Current, America/Tegucigalpa, America/Thule, America/Thunder_Bay, America/Tijuana, America/Toronto, America/Vancouver, America/Whitehorse, America/Winnipeg, America/Yakutat, America/Yellowknife]
}
