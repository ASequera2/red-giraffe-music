/*
   _____                   _____       _       _____      _   
  / ____|                 |  __ \     | |     / ____|    | |  
 | (___   ___  _ __   __ _| |__) |   _| | ___| (___   ___| |_ 
  \___ \ / _ \| '_ \ / _` |  _  / | | | |/ _ \\___ \ / _ \ __|
  ____) | (_) | | | | (_| | | \ \ |_| | |  __/____) |  __/ |_ 
 |_____/ \___/|_| |_|\__, |_|  \_\__,_|_|\___|_____/ \___|\__|
                      __/ |                                   
                     |___/                                    
An interface for defining what macro elements of the song should be set
For example, songs will have dynamics, tempo, key, introduction and conclusion rule sets
Each of which is applied to the song in turn
*/

interface SongRuleSet {
   void generateSong(Song s);
}