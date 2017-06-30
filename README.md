# Android_Audio_Player
Composant qui joue un fichier audio.

<strong>Description : </strong>

Ce composant lit un fichier audio après que le chemin du fichier lui soit parvenu par événement UPnP dans un fichier XML.

L'application étant un service Android, elle ne présente pas d'interface graphique, seulement une notification qui si activée
arrête la lecture en cours ainsi que l'application.

<strong>Lancement de l'application : </strong>

L'application ne peut pas communiquer via UPnP lorsque lancée dans un émulateur, elle doit être lancée sur un terminal physique 
et appartenir au même réseau local que les autres composants.

Il faut donc installer l'apk sur le terminal, vérifier d'avoir autorisé les sources non vérifiées.

Après démarrage de l'application, il est possible d'ajouter le composant sur wcomp en suivant la méthode décrite sur le wiki 
oppocampus.

<strong>Spécifications UPnP : </strong>

Ce composant un service UPnP dont voici la description :

  AudioFileService :
    
     1) SetAudioFilePath(String NewAudioFilePath) : action UPnP qui reçoit un message XML contenant le chemin vers le fichier 
     audio à lire, chemin qui est ensuite transmis à l'application.
     
Voici le schéma correspondant au composant :

![alt tag](https://github.com/components-upnp/Android_Audio_Player/blob/master/Android_Audio_Player.png)

<strong>Maintenance : </strong>

Le projet de l'application est un projet gradle.
