package com.azguards.app.processor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import com.azguards.app.bean.Help;
import com.azguards.app.bean.HelpAnswer;
import com.azguards.app.bean.HelpCategory;
import com.azguards.app.bean.HelpSubCategory;
import com.azguards.app.dao.HelpDao;
import com.azguards.app.dto.HelpAnswerDto;
import com.azguards.app.dto.HelpCategoryDto;
import com.azguards.app.dto.HelpDto;
import com.azguards.app.dto.HelpSubCategoryDto;
import com.azguards.app.enumeration.HelpEnum;
import com.azguards.common.lib.dto.PaginationResponseDto;
import com.azguards.common.lib.dto.PaginationUtilDto;
import com.azguards.common.lib.dto.storage.StorageDto;
import com.azguards.common.lib.dto.user.UserInitialInfoDto;
import com.azguards.common.lib.enumeration.EntitySubTypeEnum;
import com.azguards.common.lib.enumeration.EntityTypeEnum;
import com.azguards.common.lib.exception.InvokeException;
import com.azguards.common.lib.exception.NotFoundException;
import com.azguards.common.lib.exception.ValidationException;
import com.azguards.common.lib.handler.StorageHandler;
import com.azguards.common.lib.handler.UserHandler;
import com.azguards.common.lib.util.DateUtil;
import com.azguards.common.lib.util.PaginationUtil;
import com.azguards.local.config.MessageTranslator;

import lombok.extern.apachecommons.CommonsLog;

@Service
@CommonsLog
@Transactional
public class HelpProcessor {

	List<String> stopWordsForRelatedQuestions = Arrays.asList(new String[] { "a", "able", "about", "above", "abroad", "abst", "accordance", "according",
			"accordingly", "across", "act", "actually", "added", "adj", "adopted", "affected", "affecting", "affects", "after", "afterwards", "again",
			"against", "ago", "ah", "ahead", "ain't", "all", "allow", "allows", "almost", "alone", "along", "alongside", "already", "also", "although",
			"always", "am", "amid", "amidst", "among", "amongst", "amoungst", "amount", "an", "and", "announce", "another", "any", "anybody", "anyhow",
			"anymore", "anyone", "anything", "anyway", "anyways", "anywhere", "apart", "apparently", "appear", "appreciate", "appropriate", "approximately",
			"are", "aren", "arent", "aren't", "arise", "around", "as", "a's", "aside", "ask", "asking", "associated", "at", "auth", "available", "away",
			"awfully", "b", "back", "backward", "backwards", "be", "became", "because", "become", "becomes", "becoming", "been", "before", "beforehand",
			"begin", "beginning", "beginnings", "begins", "behind", "being", "believe", "below", "beside", "besides", "best", "better", "between", "beyond",
			"bill", "biol", "both", "bottom", "brief", "briefly", "but", "by", "c", "ca", "call", "came", "can", "cannot", "cant", "can't", "caption", "cause",
			"causes", "certain", "certainly", "changes", "clearly", "c'mon", "co", "co.", "com", "come", "comes", "computer", "con", "concerning",
			"consequently", "consider", "considering", "contain", "containing", "contains", "corresponding", "could", "couldnt", "couldn't", "course", "cry",
			"c's", "currently", "d", "dare", "daren't", "date", "de", "definitely", "describe", "described", "despite", "detail", "did", "didn't", "different",
			"directly", "do", "does", "doesn't", "doing", "done", "don't", "down", "downwards", "due", "during", "e", "each", "ed", "edu", "effect", "eg",
			"eight", "eighty", "either", "eleven", "else", "elsewhere", "empty", "end", "ending", "enough", "entirely", "especially", "et", "et-al", "etc",
			"even", "ever", "evermore", "every", "everybody", "everyone", "everything", "everywhere", "ex", "exactly", "example", "except", "f", "fairly",
			"far", "farther", "few", "fewer", "ff", "fifteen", "fifth", "fify", "fill", "find", "fire", "first", "five", "fix", "followed", "following",
			"follows", "for", "forever", "former", "formerly", "forth", "forty", "forward", "found", "four", "from", "front", "full", "further", "furthermore",
			"g", "gave", "get", "gets", "getting", "give", "given", "gives", "giving", "go", "goes", "going", "gone", "got", "gotten", "greetings", "h", "had",
			"hadn't", "half", "happens", "hardly", "has", "hasnt", "hasn't", "have", "haven't", "having", "he", "hed", "he'd", "he'll", "hello", "help",
			"hence", "her", "here", "hereafter", "hereby", "herein", "heres", "here's", "hereupon", "hers", "herse”", "herself", "hes", "he's", "hi", "hid",
			"him", "himse”", "himself", "his", "hither", "home", "hopefully", "how", "howbeit", "however", "how's", "hundred", "i", "I", "id", "i'd", "ie",
			"if", "ignored", "i'll", "im", "i'm", "immediate", "immediately", "importance", "important", "in", "inasmuch", "inc", "inc.", "indeed", "index",
			"indicate", "indicated", "indicates", "information", "inner", "inside", "insofar", "instead", "interest", "into", "invention", "inward", "is",
			"isn't", "it", "itd", "it'd", "it'll", "its", "it's", "itse”", "itself", "i've", "j", "just", "k", "keep", "keeps", "kept", "keys", "kg", "km",
			"know", "known", "knows", "l", "largely", "last", "lately", "later", "latter", "latterly", "least", "less", "lest", "let", "lets", "let's", "like",
			"liked", "likely", "likewise", "line", "little", "'ll", "look", "looking", "looks", "low", "lower", "ltd", "m", "made", "mainly", "make", "makes",
			"many", "may", "maybe", "mayn't", "me", "mean", "means", "meantime", "meanwhile", "merely", "mg", "might", "mightn't", "mill", "million", "mine",
			"minus", "miss", "ml", "more", "moreover", "most", "mostly", "move", "mr", "mrs", "much", "mug", "must", "mustn't", "my", "myse”", "myself", "n",
			"na", "name", "namely", "nay", "nd", "near", "nearly", "necessarily", "necessary", "need", "needn't", "needs", "neither", "never", "neverf",
			"neverless", "nevertheless", "new", "next", "nine", "ninety", "no", "nobody", "non", "none", "nonetheless", "noone", "no-one", "nor", "normally",
			"nos", "not", "noted", "nothing", "notwithstanding", "novel", "now", "nowhere", "o", "obtain", "obtained", "obviously", "of", "off", "often", "oh",
			"ok", "okay", "old", "omitted", "on", "once", "one", "ones", "one's", "only", "onto", "opposite", "or", "ord", "other", "others", "otherwise",
			"ought", "oughtn't", "our", "ours", "ourselves", "out", "outside", "over", "overall", "owing", "own", "p", "page", "pages", "part", "particular",
			"particularly", "past", "per", "perhaps", "placed", "please", "plus", "poorly", "possible", "possibly", "potentially", "pp", "predominantly",
			"present", "presumably", "previously", "primarily", "probably", "promptly", "proud", "provided", "provides", "put", "q", "que", "quickly", "quite",
			"qv", "r", "ran", "rather", "rd", "re", "readily", "really", "reasonably", "recent", "recently", "ref", "refs", "regarding", "regardless",
			"regards", "related", "relatively", "research", "respectively", "resulted", "resulting", "results", "right", "round", "run", "s", "said", "same",
			"saw", "say", "saying", "says", "sec", "second", "secondly", "section", "see", "seeing", "seem", "seemed", "seeming", "seems", "seen", "self",
			"selves", "sensible", "sent", "serious", "seriously", "seven", "several", "shall", "shan't", "she", "shed", "she'd", "she'll", "shes", "she's",
			"should", "shouldn't", "show", "showed", "shown", "showns", "shows", "side", "significant", "significantly", "similar", "similarly", "since",
			"sincere", "six", "sixty", "slightly", "so", "some", "somebody", "someday", "somehow", "someone", "somethan", "something", "sometime", "sometimes",
			"somewhat", "somewhere", "soon", "sorry", "specifically", "specified", "specify", "specifying", "state", "states", "still", "stop", "strongly",
			"sub", "substantially", "successfully", "such", "sufficiently", "suggest", "sup", "sure", "system", "t", "take", "taken", "taking", "tell", "ten",
			"tends", "th", "than", "thank", "thanks", "thanx", "that", "that'll", "thats", "that's", "that've", "the", "their", "theirs", "them", "themselves",
			"then", "thence", "there", "thereafter", "thereby", "thered", "there'd", "therefore", "therein", "there'll", "thereof", "therere", "there're",
			"theres", "there's", "thereto", "thereupon", "there've", "these", "they", "theyd", "they'd", "they'll", "theyre", "they're", "they've", "thick",
			"thin", "thing", "things", "think", "third", "thirty", "this", "thorough", "thoroughly", "those", "thou", "though", "thoughh", "thousand", "three",
			"throug", "through", "throughout", "thru", "thus", "til", "till", "tip", "to", "together", "too", "took", "top", "toward", "towards", "tried",
			"tries", "truly", "try", "trying", "ts", "t's", "twelve", "twenty", "twice", "two", "u", "un", "under", "underneath", "undoing", "unfortunately",
			"unless", "unlike", "unlikely", "until", "unto", "up", "upon", "ups", "upwards", "us", "use", "used", "useful", "usefully", "usefulness", "uses",
			"using", "usually", "uucp", "v", "value", "various", "'ve", "versus", "very", "via", "viz", "vol", "vols", "vs", "w", "want", "wants", "was",
			"wasn't", "way", "we", "wed", "we'd", "welcome", "well", "we'll", "went", "were", "we're", "weren't", "we've", "what", "whatever", "what'll",
			"whats", "what's", "what've", "when", "whence", "whenever", "when's", "where", "whereafter", "whereas", "whereby", "wherein", "wheres", "where's",
			"whereupon", "wherever", "whether", "which", "whichever", "while", "whilst", "whim", "whither", "who", "whod", "who'd", "whoever", "whole",
			"who'll", "whom", "whomever", "whos", "who's", "whose", "why", "why's", "widely", "will", "willing", "wish", "with", "within", "without", "wonder",
			"won't", "words", "world", "would", "wouldn't", "www", "x", "y", "yes", "yet", "you", "youd", "you'd", "you'll", "your", "youre", "you're", "yours",
			"yourself", "yourselves", "you've", "z", "zero" });

	@Autowired
	private HelpDao helpDAO;

	@Autowired
	private UserHandler userHandler;

	@Autowired
	private StorageHandler storageHandler;

	@Autowired
	private MessageTranslator messageTranslator;
	
	public void saveHelp(@Valid final HelpDto helpDto, final String userId) {
		log.debug("Inside saveHelp() method");
		try {
			log.info("Calling DAO layer to save Help data in DB");
			helpDAO.save(convertDtoToazguardsHelp(helpDto, null, userId));
		} catch (Exception exception) {
			log.error("Exception while adding help data in DB having exception = "+exception);
		}
	}

	public Help convertDtoToazguardsHelp(final HelpDto dto, final String id, final String userId) {
		Help help = null;
		if (id != null) {
			help = helpDAO.get(id);
		} else {
			help = new Help();
			help.setCreatedOn(DateUtil.getUTCdatetimeAsDate());
			help.setStatus(HelpEnum.NOTASSIGNED.toString());
		}
		help.setUserId(userId);
		help.setCategory(helpDAO.getHelpCategory(dto.getCategoryId()));
		help.setSubCategory(helpDAO.getHelpSubCategory(dto.getSubCategoryId()));
		help.setCreatedBy("API");
		help.setUpdatedBy("API");
		help.setTitle(dto.getTitle());
		help.setDescription(dto.getDescription());
		help.setUpdatedOn(DateUtil.getUTCdatetimeAsDate());
		help.setIsActive(true);
		help.setIsQuestioning(dto.getIsQuestioning());
		return help;
	}

	public HelpDto getazguardsHelp(final String id) throws NotFoundException {
		log.debug("Inside getazguardsHelp() method");
		HelpDto helpDto = null;
		log.info("Extracting azguards Help data from DB for id = "+id);
		helpDto = convertazguardsHelpToDto(helpDAO.get(id));
		if (ObjectUtils.isArray(helpDto)) {
			log.error(messageTranslator.toLocale("help.id.notfound",id,Locale.US));
			throw new NotFoundException(messageTranslator.toLocale("help.id.notfound",id));
		}
		return helpDto;
	}

	public HelpDto convertazguardsHelpToDto(final Help help) {
		log.debug("Inside convertSeekaHelpToDto() method");
		HelpDto helpDto = new HelpDto();
		helpDto.setId(help.getId());
		helpDto.setTitle(help.getTitle());
		helpDto.setDescription(help.getDescription());
		helpDto.setCategoryId(help.getCategory().getId());
		helpDto.setSubCategoryId(help.getSubCategory().getId());
		helpDto.setIsQuestioning(help.getIsQuestioning());
		helpDto.setStatus(help.getStatus());
		if (help.getUserId() != null) {
			try {
				log.info("Calling Identity Service to fetch userInfo for userId "+help.getUserId());
				UserInitialInfoDto userDto = userHandler.getUserById(help.getUserId());
				if (userDto != null) {
					if (userDto.getFirstName() != null) {
						helpDto.setCreatedUser(userDto.getFirstName());
					}
					if ((userDto.getFirstName() != null) && (userDto.getLastName() != null)) {
						helpDto.setCreatedUser(userDto.getFirstName() + " " + userDto.getLastName());
					}
				}
			} catch (Exception e) {
				log.error("Exception while calling Identity Service having exception = "+e);
			}
		}
		return helpDto;
	}

	public void updateHelp(final HelpDto helpDto, final String id, final String userId) {
		log.debug("Inside updateHelp() method");
		try {
			log.info("Calling DAO layer to update help data in DB");
			helpDAO.update(convertDtoToazguardsHelp(helpDto, id, userId));
		} catch (Exception exception) {
			log.error("Exception while updating Help having exception = "+exception);
		}
	}

	public PaginationResponseDto getAll(final Integer pageNumber, final Integer pageSize) {
		log.debug("Inside getAll() method");
		PaginationResponseDto paginationResponseDto = null;
		try {
			log.info("Extracting total count of helps data from DB");
			int totalCount = helpDAO.findTotalHelpRecord(null, null);
			Long startIndex = (Long.valueOf(pageNumber - 1)) * pageSize;
			log.info("Calculating Pagination on based on startIndex, pageSize and totalCount");
			PaginationUtilDto paginationUtilDto = PaginationUtil.calculatePagination(startIndex, pageSize, totalCount);
			log.info("Extracting all helps data from DB based on pagination");
			List<Help> helps = helpDAO.getAll(startIndex.intValue(), pageSize, null, null);
			List<HelpDto> helpDto = new ArrayList<>();
			if(!CollectionUtils.isEmpty(helps)) {
				helps.stream().forEach(help -> {
					helpDto.add(convertazguardsHelpToDto(help));
				});
			}
			
			paginationResponseDto = new PaginationResponseDto();
			paginationResponseDto.setHasNextPage(paginationUtilDto.isHasNextPage());
			paginationResponseDto.setHasPreviousPage(paginationUtilDto.isHasPreviousPage());
			paginationResponseDto.setPageNumber(paginationUtilDto.getPageNumber());
			paginationResponseDto.setTotalCount(Long.valueOf(totalCount));
			paginationResponseDto.setTotalPages(paginationUtilDto.getTotalPages());
			paginationResponseDto.setResponse(helpDto);
		} catch (Exception exception) {
			log.error("Exception while fetching helps data ="+exception);
		}
		return paginationResponseDto;
	}

	public void saveHelpCategory(@Valid final HelpCategoryDto helpCategoryDto) {
		log.debug("Inside saveHelpCategory() method");
		try {
			log.info("Calling DAO layer to add helpCategory data in DB");
			helpDAO.save(convertHelpCategoryDtoToBean(helpCategoryDto));
		} catch (Exception exception) {
			log.error("Exception while adding helpCategory in DB having exception "+exception);
		}
	}

	public HelpCategory convertHelpCategoryDtoToBean(final HelpCategoryDto helpCategoryDto) {
		HelpCategory helpCategory = new HelpCategory();
		helpCategory.setName(helpCategoryDto.getName());
		helpCategory.setCreatedBy("API");
		helpCategory.setUpdatedBy("API");
		helpCategory.setCreatedOn(DateUtil.getUTCdatetimeAsDate());
		helpCategory.setUpdatedOn(DateUtil.getUTCdatetimeAsDate());
		helpCategory.setIsActive(true);
		return helpCategory;
	}

	public void saveHelpSubCategory(@Valid final HelpSubCategoryDto helpSubCategoryDto) {
		log.debug("Inside saveHelpSubCategory() method");
		try {
			log.info("Calling DAO layer to add helpSubCategory data in DB");
			helpDAO.save(convertHelpCategoryDtoToBean(helpSubCategoryDto));
		} catch (Exception exception) {
			log.error("Exception while adding helpSubCategory in DB having exception ");
		}
	}

	public HelpSubCategory convertHelpCategoryDtoToBean(final HelpSubCategoryDto helpSubCategoryDto) {
		HelpSubCategory helpSubCategory = new HelpSubCategory();
		helpSubCategory.setName(helpSubCategoryDto.getName());
		helpSubCategory.setCreatedBy("API");
		helpSubCategory.setUpdatedBy("API");
		helpSubCategory.setCreatedOn(DateUtil.getUTCdatetimeAsDate());
		helpSubCategory.setUpdatedOn(DateUtil.getUTCdatetimeAsDate());
		helpSubCategory.setHelpCategory(helpDAO.getHelpCategory(helpSubCategoryDto.getCategoryId()));
		helpSubCategory.setIsActive(true);
		return helpSubCategory;
	}

	public HelpCategoryDto getCategory(final String id) {
		log.debug("Inside getCategory() method");
		HelpCategoryDto helpCategoryDto = null;
		try {
			log.info("Extracting help category data from DB having id "+id);
			helpCategoryDto = convertHelpCategoryToDto(helpDAO.getHelpCategory(id));
		} catch (Exception exception) {
			log.error("Exception while fetching category having exception "+exception);
		}
		return helpCategoryDto;
	}

	public HelpCategoryDto convertHelpCategoryToDto(final HelpCategory helpCategory) {
		HelpCategoryDto helpCategoryDto = new HelpCategoryDto();
		helpCategoryDto.setName(helpCategory.getName());
		helpCategoryDto.setId(helpCategory.getId());
		return helpCategoryDto;
	}

	public HelpSubCategoryDto getSubCategory(final String id) {
		log.debug("Inside getSubCategory() method");
		HelpSubCategoryDto helpSubCategoryDto = null;
		try {
			log.info("Extracting help SubCategory data from DB having id "+id);
			helpSubCategoryDto = convertHelpSubCategoryToDto(helpDAO.getHelpSubCategory(id), 0);
		} catch (Exception exception) {
			log.error("Exception while fetching sub-category having exception "+exception);
		}
		return helpSubCategoryDto;
	}

	public HelpSubCategoryDto convertHelpSubCategoryToDto(final HelpSubCategory helpSubCategory, Integer subCategoryCount) {
		HelpSubCategoryDto helpSubCategoryDto = new HelpSubCategoryDto();
		helpSubCategoryDto.setCategoryId(helpSubCategory.getHelpCategory().getId());
		helpSubCategoryDto.setName(helpSubCategory.getName());
		helpSubCategoryDto.setId(helpSubCategory.getId());
		helpSubCategoryDto.setHelpCount(subCategoryCount);
		return helpSubCategoryDto;
	}

	public List<HelpSubCategoryDto> getSubCategoryByCategory(final String categoryId, final Integer startIndex, final Integer pageSize) {
		log.debug("Inside getSubCategoryByCategory() method");
		List<HelpSubCategoryDto> subCategoryDtos = new ArrayList<>();
		log.info("Extracting SubCategory data having categoryId "+categoryId);
		List<HelpSubCategory> categories = helpDAO.getSubCategoryByCategory(categoryId, startIndex, pageSize);
		for (HelpSubCategory helpSubCategory : categories) {
			log.info("Fetching total count of subCategories having subCategoryId "+helpSubCategory.getId());
			Integer subCategoryCount = helpDAO.findTotalHelpRecordBySubCategory(helpSubCategory.getId());
			subCategoryDtos.add(convertHelpSubCategoryToDto(helpSubCategory, subCategoryCount));
		}
		return subCategoryDtos;
	}

	public List<HelpDto> getHelpByCategory(final String categoryId) {
		log.debug("Inside getHelpByCategory() method"); 
		List<HelpDto> helpDtos = new ArrayList<>();
		log.info("Extracting Help for categoryId "+categoryId);
		List<Help> seekHelps = helpDAO.getHelpByCategory(categoryId);
		try {
			for (Help seekaHelp : seekHelps) {
				helpDtos.add(convertazguardsHelpToDto(seekaHelp));
			}
		} catch (Exception exception) {
			log.error("Excpetion while fetching data having exception = "+exception);
		}
		return helpDtos;
	}

	public List<HelpSubCategoryDto> getSubCategoryCount() {
		log.debug("Inside getSubCategoryCount() method"); 
		List<HelpSubCategoryDto> helpSubCategories = new ArrayList<>();
		try {
			log.info("Extracting all help subCategories from DB");
			List<HelpSubCategory> subCatgories = helpDAO.getAllHelpSubCategories();
			for (HelpSubCategory helpSubCategory : subCatgories) {
				log.info("Fetching total HelpRecord for subCtaegory "+helpSubCategory.getId());
				Integer subCategoryCount = helpDAO.findTotalHelpRecordBySubCategory(helpSubCategory.getId());
				helpSubCategories.add(convertHelpSubCategoryToDto(helpSubCategory, subCategoryCount));
			}
		} catch (Exception exception) {
			log.error("Exception while fetching subCategoryCount having exception "+exception);
		}
		return helpSubCategories;
	}

	public void saveAnswer(@Valid final HelpAnswerDto helpAnswerDto, final MultipartFile file) throws Exception {
		log.debug("Inside saveAnswer() method");
		try {
			log.info("Calling DAO layer to sabe HelpAnswer in DB");
			HelpAnswer helpAnswer = helpDAO.save(convertDtoToHelpAnswerBeans(helpAnswerDto));
			if ((helpAnswer != null) && (file != null)) {
				log.info("Uploading image on Storage Service for helpAnswerId " + helpAnswer.getId());
				String logoName = storageHandler.uploadFileInStorage(file, helpAnswer.getId(),
						EntityTypeEnum.HELP_SUPPORT, EntitySubTypeEnum.IMAGES);
				log.info("Help answer media upload for id - >" + helpAnswer.getId() + " and Image  name :" + logoName);
				if ((logoName != null) && !logoName.isEmpty() && !logoName.equals("null")) {
					helpAnswer.setFileName(logoName);
					log.info("Calling DAO layer to update Answer in DB");
					helpDAO.updateAnwser(helpAnswer);
				}
			}
		} catch (Exception exception) {
			log.error("Exception while adding answer having exception "+exception);
			throw exception;
		}
	}

	public HelpAnswer convertDtoToHelpAnswerBeans(final HelpAnswerDto answerDto) {
		HelpAnswer helpAnswer = new HelpAnswer();
		helpAnswer.setAnswer(answerDto.getAnswer());
		helpAnswer.setHelp(helpDAO.get(answerDto.getHelpId()));
		helpAnswer.setUser(answerDto.getUserId());
		helpAnswer.setCreatedOn(DateUtil.getUTCdatetimeAsDate());
		helpAnswer.setCreatedBy("API");
		helpAnswer.setUpdatedOn(DateUtil.getUTCdatetimeAsDate());
		helpAnswer.setUpdatedBy("API");
		helpAnswer.setIsDeleted(false);
		return helpAnswer;
	}

	public List<HelpAnswerDto> getAnswerByHelpId(final String helpId) throws NotFoundException {
		log.debug("Inside getAnswerByHelpId() method");
		List<HelpAnswerDto> helpAnswerDtos = new ArrayList<>();
		log.info("Extracting helpAnswer for helpId "+helpId);
		List<HelpAnswer> helpAnswers = helpDAO.getAnswerByHelpId(helpId);
		if(!CollectionUtils.isEmpty(helpAnswers)) {
			log.info("Help Answers fetched from DB, start iterating data");
			helpAnswers.stream().forEach(helpAnswer -> {
				try {
					helpAnswerDtos.add(convertBeanToHelpAnswerDto(helpAnswer));
				} catch (NotFoundException | InvokeException e) {
					log.error("Exception while convertBeanToHelpAnswerDto :" + e.getMessage());
				}
			});
		} else {
			log.error(messageTranslator.toLocale("help.id.notfound",helpId,Locale.US));
			throw new NotFoundException(messageTranslator.toLocale("help.id.notfound",helpId));
		}
		return helpAnswerDtos;
	}

	public HelpAnswerDto convertBeanToHelpAnswerDto(final HelpAnswer helpAnswer) throws NotFoundException, InvokeException {
		log.debug("Inside convertBeanToHelpAnswerDto() method");
		HelpAnswerDto helpAnswerDto = new HelpAnswerDto();
		helpAnswerDto.setAnswer(helpAnswer.getAnswer());
		helpAnswerDto.setUserId(helpAnswer.getUser());
		helpAnswerDto.setHelpId(helpAnswer.getHelp().getId());
		if (helpAnswer.getFileName() != null) {
			log.info("Calling Storage Service to fetch helpSupport images for helpAnswerId "+helpAnswer.getId());
			List<StorageDto> storageDTOList = storageHandler.getStorages(helpAnswer.getId(), EntityTypeEnum.HELP_SUPPORT, EntitySubTypeEnum.IMAGES);
			if (!CollectionUtils.isEmpty(storageDTOList)) {
				StorageDto storageDto = storageDTOList.get(0);
				if (storageDto != null) {
					helpAnswerDto.setFileUrl(storageDto.getFileURL());
				}
			}
		}
		return helpAnswerDto;
	}

	public List<HelpCategoryDto> getCategory(final Integer startIndex, final Integer pageSize) {
		log.debug("Inside getCategory() method");
		List<HelpCategoryDto> dtos = new ArrayList<>();
		log.info("Extracting all help categories from DB based on pagination");
		List<HelpCategory> categories = helpDAO.getAllCategory(startIndex, pageSize);
		if(!CollectionUtils.isEmpty(categories)) {
			log.info("Help Categories fetched from DB, start iterating data");
			categories.stream().forEach(category -> {
				dtos.add(convertHelpCategoryToDto(category));
			});
		}
		return dtos;
	}

	public void delete(@Valid final String id) throws NotFoundException {
		log.debug("Inside delete() method");
		log.info("Extracting Help data from DB for helpId " + id);
		Help help = helpDAO.get(id);
		if (!ObjectUtils.isEmpty(help)) {
			help.setDeletedOn(DateUtil.getUTCdatetimeAsDate());
			help.setUpdatedOn(DateUtil.getUTCdatetimeAsDate());
			help.setIsActive(false);
			log.info("Calling DAO layer to update existing help data in DB");
			helpDAO.update(help);
		} else {
			log.error(messageTranslator.toLocale("help.id.notfound",id,Locale.US));
			throw new NotFoundException(messageTranslator.toLocale("help.id.notfound",id));
		}
	}

	public void updateStatus(final String id, final String userId, final String status) throws NotFoundException {
		log.debug("Inside updateStatus() method");
		log.info("Extracting Help data from DB for helpId " + id);
		Help help = helpDAO.get(id);
		if (!ObjectUtils.isEmpty(help)) {
			help.setUpdatedOn(DateUtil.getUTCdatetimeAsDate());
			help.setStatus(status);
			if (userId != null) {
				help.setAssignedUserId(userId);
			}
			log.info("Calling DAO layer to update existing help data in DB");
			helpDAO.update(help);
		} else {
			log.error(messageTranslator.toLocale("help.id.notfound",id,Locale.US));
			throw new NotFoundException(messageTranslator.toLocale("help.id.notfound",id));
		}
	}

	public List<HelpDto> filter(final String status, final String mostRecent, final String categoryId) {
		log.debug("Inside filter() method");
		List<HelpDto> helpDtos = new ArrayList<>();
		try {
			List<Help> azguardsHelps = new ArrayList<>();
			if ((status != null) && !status.isEmpty()) {
				log.info("Filtering helps data based on status "+ status + " and categoryId "+categoryId);
				azguardsHelps = helpDAO.findByStatus(status, categoryId);
			}
			if ((mostRecent != null) && !mostRecent.isEmpty()) {
				log.info("Filtering helps data based on mostRecent "+ mostRecent + " and categoryId "+categoryId);
				azguardsHelps = helpDAO.findByMostRecent(mostRecent, categoryId);
			}
			if ((status != null) && !status.isEmpty() && (mostRecent != null) && !mostRecent.isEmpty()) {
				log.info("Filtering helps data based on mostRecent "+ mostRecent + " and categoryId "
						+ categoryId + " and status "+status);
				azguardsHelps = helpDAO.findByStatusAndMostRecent(status, mostRecent, categoryId);
			}
			if ((status == null) && (mostRecent == null)) {
				log.info("Filtering helps data based on categoryId "+categoryId);
				azguardsHelps = helpDAO.getHelpByCategory(categoryId);
			}
			azguardsHelps.stream().forEach(seekaHelp -> {
				helpDtos.add(convertazguardsHelpToDto(seekaHelp));
			});
		} catch (Exception exception) {
			log.error("Exception while filtering helps having exception "+exception);
		}
		return helpDtos;
	}

	public List<HelpDto> getUserHelpList(final String userId, final Long startIndex, final Integer pageSize, final Boolean isArchive) {
		List<HelpDto> helpDto = new ArrayList<>();
		List<Help> helpFromDB = helpDAO.getAll(startIndex.intValue(), pageSize, userId, isArchive);
		if(!CollectionUtils.isEmpty(helpFromDB)) {
			helpFromDB.stream().forEach(help -> {
				helpDto.add(convertazguardsHelpToDto(help));
			});
		}
		return helpDto;
	}

	public int getUserHelpCount(final String userId, final Boolean isArchive) {
		return helpDAO.findTotalHelpRecord(userId, isArchive);
	}

	public void setIsFavouriteFlag(final String id, final boolean isFavourite) throws NotFoundException {
		helpDAO.setIsFavouriteFlag(id, isFavourite);
	}

	public int getCategoryCount() {
		return helpDAO.getCategoryCount();
	}

	public int getSubCategoryCount(final String categoryId) {
		return helpDAO.getSubCategoryCount(categoryId);
	}

	public void archiveHelpSupport(final String entityId, final boolean isArchive) {
		Help seekaHelp = helpDAO.get(entityId);
		seekaHelp.setIsArchive(isArchive);
		seekaHelp.setUpdatedBy("API");
		seekaHelp.setUpdatedOn(new Date());
		helpDAO.update(seekaHelp);
	}

	public List<String> getRelatedSearchQuestions(String searchString) throws ValidationException {
		searchString = searchString.toLowerCase();
		List<String> searchKeywords = new LinkedList<>(Arrays.asList(searchString.split(" ")));
		searchKeywords.removeAll(stopWordsForRelatedQuestions);
		if ((searchKeywords == null) || searchKeywords.isEmpty()) {
			throw new ValidationException(messageTranslator.toLocale("help.keyword.proper"));
		}
		return helpDAO.getRelatedSearchQuestions(searchKeywords);
	}
}
