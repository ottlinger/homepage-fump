 package  com.fump;
     import  java.awt.event.*;
     import  javax.swing.*;
    import   java.awt.*;
    import   javax.swing.border.*;
     class MyMouseListener   implements MouseListener {
         public    void        mouseClicked(MouseEvent e)  {}

         public void    mouseEntered(MouseEvent e) {
              ((JButton) e.getSource()).setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));

              }

     public void mouseExited(MouseEvent e)   {
            ((JButton) e.getSource()).setBorder(BorderFactory.createEmptyBorder(4,4,4,4));

     }

         public void  mousePressed(MouseEvent e) {
            ((JButton) e.getSource()).setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

        }

       public void   mouseReleased(MouseEvent e)  {
           ((JButton) e.getSource()).setBorder(BorderFactory.createEmptyBorder());
      }
  }

