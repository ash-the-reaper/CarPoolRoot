package ashwin.uomtrust.ac.mu.service;



import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ashwin.uomtrust.ac.mu.dto.MessageDTO;
import ashwin.uomtrust.ac.mu.entity.Account;
import ashwin.uomtrust.ac.mu.entity.Device;
import ashwin.uomtrust.ac.mu.entity.Message;
import ashwin.uomtrust.ac.mu.repository.AccountRepository;
import ashwin.uomtrust.ac.mu.repository.DeviceRepository;
import ashwin.uomtrust.ac.mu.repository.MessageRepository;
import ashwin.uomtrust.ac.mu.utils.PushNotifictionHelper;
import ashwin.uomtrust.ac.mu.utils.Utils;



@Service
public class MessageServiceImp implements MessageService{
	
	@Autowired
	private MessageRepository messageRepository;
	
	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private DeviceRepository deviceRepository;

	@Override
	public MessageDTO saveMessage(MessageDTO messageDTO) {
		// TODO Auto-generated method stub
		Message message = new Message();
		
		Account account = accountRepository.findByAccountId(messageDTO.getAccountId());
		message.setAccount(account);
		message.setOtherUserId(messageDTO.getOtherUserId());
		message.setMessage(messageDTO.getMessage());
		
		Message newMessage = messageRepository.save(message);
		messageDTO.setMessageId(newMessage.getMessageId());
		
		List<Device> deviceList = deviceRepository.getDeviceByAccountId(messageDTO.getOtherUserId());

		for(Device device :deviceList){
			String title = "CHAT";
			PushNotifictionHelper.sendPushNotification(device.getDeviceToken(),title, messageDTO.getMessage());
		}
		
		return messageDTO;
	}

	@Override
	public List<MessageDTO> getMessages(MessageDTO messageDTO) {
		// TODO Auto-generated method stub
		
		List<Message> messagesSent = messageRepository.getMessagesSentByUser(messageDTO.getAccountId());
		List<Message> messagesReceived = messageRepository.getMessagesReceived(messageDTO.getAccountId());

		
		List<MessageDTO> messageDTOList = new ArrayList<>();
		
		for(Message m : messagesSent){
			MessageDTO newMessageDTO = new MessageDTO();	
			
			Account account = m.getAccount();			
			newMessageDTO.setAccountId(account.getAccountId());
			newMessageDTO.setMessageId(m.getMessageId());
			newMessageDTO.setMessage(m.getMessage());
			newMessageDTO.setOtherUserId(m.getOtherUserId());
			
			Account acc = accountRepository.findByAccountId(m.getOtherUserId());
			newMessageDTO.setSenderFullName(acc.getFullName());
			messageDTOList.add(newMessageDTO);
		}
		
		for(Message m : messagesReceived){
			MessageDTO newMessageDTO = new MessageDTO();	
			
			Account account = m.getAccount();			
			newMessageDTO.setAccountId(account.getAccountId());
			newMessageDTO.setMessageId(m.getMessageId());
			newMessageDTO.setMessage(m.getMessage());
			newMessageDTO.setOtherUserId(m.getOtherUserId());
			newMessageDTO.setSenderFullName(account.getFullName());
			Utils.getImageProfile(newMessageDTO);
			messageDTOList.add(newMessageDTO);
		}
		
		return messageDTOList;
	}
	
}
