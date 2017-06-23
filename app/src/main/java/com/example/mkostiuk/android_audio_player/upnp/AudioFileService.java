package com.example.mkostiuk.android_audio_player.upnp;

import com.example.mkostiuk.android_audio_player.xml.LecteurXml;

import org.fourthline.cling.binding.annotations.UpnpAction;
import org.fourthline.cling.binding.annotations.UpnpInputArgument;
import org.fourthline.cling.binding.annotations.UpnpService;
import org.fourthline.cling.binding.annotations.UpnpServiceId;
import org.fourthline.cling.binding.annotations.UpnpServiceType;
import org.fourthline.cling.binding.annotations.UpnpStateVariable;
import org.xml.sax.SAXException;

import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by mkostiuk on 23/06/2017.
 */

@UpnpService(
        serviceType = @UpnpServiceType(value = "AudioFileService", version = 1),
        serviceId = @UpnpServiceId("AudioFileService")
)
public class AudioFileService {

    private final PropertyChangeSupport propertyChangeSupport;

    public AudioFileService() {
        propertyChangeSupport = new PropertyChangeSupport(this);
    }

    public PropertyChangeSupport getPropertyChangeSupport() {
        return propertyChangeSupport;
    }

    @UpnpStateVariable(name = "AudioFilePath")
    private String audioFilePath = "";

    @UpnpAction(name = "SetAudioFilePath")
    public void setAudioFilePath(@UpnpInputArgument(name = "NewAudioFilePath") String a) throws IOException, SAXException, ParserConfigurationException {
        audioFilePath = a;

        if (!audioFilePath.equals("")) {
            LecteurXml l = new LecteurXml(audioFilePath);

            HashMap<String,String> args = new HashMap<>();
            args.put("UDN",l.getUdn());
            args.put("FILEPATH",l.getFilePath());

            getPropertyChangeSupport().firePropertyChange("path", "", args);
        }
    }
}
