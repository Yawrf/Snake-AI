# Snake-AI
Java coded Snake game under optional AI control

 -- DumbAI
 
 -- NeuralNet
 
 -- UserControl


To enable DumbAI:
 - Set generational False (FXMLDocumentController:51)
 - Enable (uncomment) SnakeObject:265
 - Disable (comment) SnakeObject:266-267


To enable NeuralNet:
 - Set generational True (FXMLDocumentController:51)
 - Enable (uncomment) SnakeObject:266-267
 - Disable (comment) SnakeObject:265


To enable UserControl
 - Set generational False (FXMLDocumentController:51)
 - Disable (comment) SnakeObject:265-267


A Word of Caution:
  Java likes to make internal errors when you set the snakeCount (FXMLDocumentController:39) too high. I'm keeping it at 750, 1000 was freezing for me.
  
  
To check out the code of my (really crappy) NeuralNet project, try looking here:
 - https://github.com/Yawrf/NeuralNet
