Option Explicit

' Defines global variables
' ArrayLists
Dim depthList As Object, pyList As Object, pyListLocal As Object, pyModifiersDirectionOne As Object, pyModifiersDirectionTwo As Object
Dim userPreferenceInclusion As Object, userPreferenceName As Object
' Other variables
Dim fileName As String, linkNameCustom As String, linkNameDivider As String
Dim directionOne As String, directionTwo As String
Dim linkNumbering As Long, directionPreference As Long
Dim outputPyTable As Boolean

' @description("This is the main subroutine to process PY spring")
Public Sub Main()
    ' Note from Daniel:
    
    ' Check if inputs are valid
    CheckUserInput
    
    UpdateSystemStatus "Processing..."

    ' Process user input
    View

    ' Read from input file
    ReadDataFSM False
    
    'Define where to read the user modifiers
    Dim startCellRow As Long, startCellCol As Long
    Set pyModifiersDirectionOne = CreateObject("System.Collections.ArrayList")
    Set pyModifiersDirectionTwo = CreateObject("System.Collections.ArrayList")
    
    startCellRow = 32
    startCellCol = 17
    
    ReadUserModifiers startCellRow, startCellCol
    
    ' Output to a table
    If outputPyTable = True Then
        OutputPySpring 1, 1
    End If
    
    ' Output SAP Links
    PrintSAPOutput 0, linkNumbering, directionPreference
    
    MsgBox "Success! Process complete. Check sheet 'PY Spring' and 'SAP Output' "
    
    UpdateSystemStatus "Process complete!"
    
End Sub
' @Description("This method prompt user to add in a LPile output for post-processing")
Public Sub ChooseFile()

    ClearContents
    
    UpdateSystemStatus "Selecting Files..."

    GetFileName
    
    View
    
    ReadDepthFromLpile
    
    MsgBox "Success! Input file loaded. Please modify P-factors before run."
    
    UpdateSystemStatus "File Loaded"

End Sub

' @Description("MAIN METHOD: This method is reads the pile depth and output data on to the main page")
Public Sub ReadDepthFromLpile()

    CheckUserInput

    ' Read fileName
    View

    ' Read the list of depth from the input file
    ReadDataFSM True
    
    ' Print out depth on the sheet
    Dim startCellRow As Long
    Dim startCellCol As Long
    
    startCellRow = 32
    startCellCol = 2
    
    PrintDepthData 29, 1
    
    UpdateSystemStatus "Depth Loaded"
    
    'MsgBox "Success! Input file loaded. Please modify P-factors before run."


End Sub

' @Description("This method extract any input from the user interface")
Private Sub View()

    With Worksheets("Main Page")
    
        ' Grab parameters
        
        directionOne = .Cells(14, 3)
        directionTwo = .Cells(15, 3)
        
        fileName = .Cells(8, 2).Value
        linkNameCustom = .Cells(19, 4).Value
        linkNameDivider = .Cells(20, 4).Value
        
        If .Cells(21, 4).Value = "Depth below pile head" Then
            linkNumbering = 1
        ElseIf .Cells(21.4).Value = "Index based on processing" Then
            linkNumbering = 0
        End If
        
        If .Cells(4, 25).Value = 1 Then
            directionPreference = 2
        ElseIf .Cells(5, 25).Value = 1 Then
            directionPreference = 3
        Else
            directionPreference = 1
        End If
        
        If .Cells(6, 25) = "Yes" Then
            outputPyTable = True
        Else
            outputPyTable = False
        End If
        
    End With
    
End Sub

' @Description("This method opens up file dialog and let user select a file")
Private Sub GetFileName()

    Dim filePathSelect As String
    Dim myFile As FileDialog
    Set myFile = Application.FileDialog(msoFileDialogOpen)
    
    With myFile
        .title = "Select LPile Output File"
        .AllowMultiSelect = False
        If .Show <> -1 Then
            UpdateSystemStatus "File Secection Cancelled. Re-select files to run."
            End
        End If
        
        filePathSelect = .SelectedItems(1)
    End With

    Worksheets("Main Page").Cells(8, 2) = filePathSelect

End Sub

' @Description("A basic userinput check")
Private Sub CheckUserInput()

    Dim fileName As String
    Dim fileExists As String
    
    fileExists = Dir(Worksheets("Main Page").Cells(8, 2))
    
    If fileExists = "" Then
        DisplayMessage "Invalid File Path!"
        End
    End If
    
End Sub

' @Description("This method prints out PY Spring data for each depth")
' Upon processing the input file, each PY Spring data is stored in an Arraylist of ArrayLists.
' The following code is to unpack the data structure while reverse the PY spring data for SAP input
'
' https://superuser.com/questions/988321/which-excel-objects-are-zero-based-and-which-are-one-based
Private Sub OutputPySpring(printRow As Long, printCol As Long)

    ' TODO: Refine the passing paramters so it's no so hard coded
    
    ' Plot out raw output without modifiers
    printPySpringHelper "Raw p-y Spring Output for Each Depth", 1, 1, 0
    
    ' Plot output with Ux modifiers
    printPySpringHelper "Processed p-y Spring Output for Each Depth of " & directionOne & " with Modifiers", 1, 41, 1
    
    ' Plot output with Uy modifiers
    printPySpringHelper "Processed p-y Spring Output for Each Depth of " & directionTwo & " with Modifiers", 1, 81, 2

End Sub

' @Description("Private helper for printing the PYSpring data in sheet 'PY Spring'")
' This method will input any necessary data to populate a new set of py spring data
Private Sub printPySpringHelper(title As String, startCol As Long, startRow As Long, modifierDir As Long)

    Dim depthTitle(4) As String
    Dim depthloop As Long, pyLoop As Long
    Dim modifier As Long
    
    depthTitle(0) = "Elev (ft) ="
    depthTitle(2) = "y, lb"
    depthTitle(3) = "p, lbs/in"
    
    With Worksheets("PY Spring")
    
        ' Generate title
        .Cells(startRow, startCol) = title
        
        ' Generate table. The table shall begin 2 rows below the title
            startRow = startRow + 2
    
         ' loop through each depth
        For depthloop = 0 To depthList.Count - 1
        
            ' Generate table headers
            .Cells(startRow, startCol) = depthTitle(0)
            .Cells(startRow, startCol + 1) = depthList.Item(depthloop)(1)
            .Cells(startRow + 1, startCol) = depthTitle(2)
            .Cells(startRow + 1, startCol + 1) = depthTitle(3)
            
            ' Mark anything that user decided to exclude
            If (userPreferenceInclusion(depthloop) = 1) Then
            
                .Cells(startRow - 1, startCol) = "Excluded"
                .Cells(startRow - 1, startCol + 1) = "Excluded"
            
            End If
            
            
            ' Find out what modifier it should use for the p data
            Select Case modifierDir
                Case 0
                    modifier = 1 'Nothing to multiply. Just the raw data
                Case 1
                    modifier = pyModifiersDirectionOne(depthloop) 'For Ux spring, grab the modifier at that depth
                Case 2
                    modifier = pyModifiersDirectionTwo(depthloop) 'For Uy spring, grab the modifier at that depth
                Case Else
                    modifier = 1 ' by default no input is selected (which shouldn't happen)
            End Select
            
            ' Output actual PY Spring data
            ' Within each depth, loop through each entry of within that depth
            For pyLoop = 0 To pyList.Item(depthloop).Count - 1
            
                .Cells(startRow + 2 + pyLoop, startCol) = pyList.Item(depthloop)(pyLoop)(0) 'the y data
                .Cells(startRow + 2 + pyLoop, startCol + 1) = pyList.Item(depthloop)(pyLoop)(1) * modifier 'the p data
            
            Next pyLoop
            
            ' Once one depth is looped through, go to the next depth and update the starting column
            startCol = startCol + 2
            
        Next depthloop
    
    End With

End Sub



' @Description ("This method prints out depth data from the LPile output for user to process")
Private Sub PrintDepthData(startRow As Long, startCol As Long)

    Dim depthCount As Long, loopArray As Long
    
    With Worksheets("Main Page")
    
        For depthCount = 0 To depthList.Count - 1
        
            For loopArray = 0 To 2
                .Cells(startRow + depthCount, startCol + loopArray) = depthList(depthCount)(loopArray)
            Next loopArray
        
        Next depthCount
    
    End With
    
    Worksheets("Developer").Cells(25, 23) = depthList.Count

End Sub

'@Description("This sub reads the user modifiers such as trib height, and other factors")
Private Sub ReadUserModifiers(startRow As Long, startCol As Long)

    Dim depthCount As Long
    
    Set userPreferenceInclusion = CreateObject("System.Collections.ArrayList")
    Set userPreferenceName = CreateObject("System.Collections.ArrayList")

    With Worksheets("Developer")
    
        For depthCount = 0 To depthList.Count - 1
        
            pyModifiersDirectionOne.Add (.Cells(startRow + depthCount, startCol))
            pyModifiersDirectionTwo.Add (.Cells(startRow + depthCount, startCol + 1))
            
            ' Obtain user overrides
            userPreferenceInclusion.Add .Cells(32 + depthCount, 19)
            userPreferenceName.Add .Cells(32 + depthCount, 20)
        
        Next depthCount
        
    End With

End Sub

Private Sub PrintSAPOutput(printPreference As Long, namePreference As Long, directionPreference As Long)

    ' SAP Parameters
    Dim sapSetting(4) As String
    Dim printDirOne As Boolean, printDirTwo As Boolean
    Dim depthloop As Long
    Dim startRow As Long
    Dim rowCountPerDepth As Long
    
    sapSetting(0) = "No"            'If fixed
    sapSetting(1) = "Yes"           'Is nonlinear
    sapSetting(2) = "0"             'TransKE
    sapSetting(3) = "0"             'TransCE
    
    printDirOne = True
    printDirTwo = True
    
    startRow = 4
    
    If directionPreference = 2 Then
        printDirTwo = False             'Direction One only
    ElseIf directionPreference = 3 Then
        printDirOne = False             'Direction Two only
    End If
    
    ' The idea is to loop through each depth of the spring, for depth loop thru the py data, and print out Ux an Uy
    For depthloop = 0 To depthList.Count - 1
    
        ' Short ciruit, check if user wants to output this string
        If userPreferenceInclusion(depthloop) = 1 Then
            ' I hate the fact that we have to use some work-around code on this outdated language
            GoTo SkipCurrentLoopToPrintSAPSpring
            
        End If
    
        ' determine if we need Ux, Uy or both Ux and UySelect Case modifierDir
        Select Case printPreference
            Case 1
                rowCountPerDepth = pyList.Item(depthloop).Count 'For Ux spring
            Case 2
                rowCountPerDepth = pyList.Item(depthloop).Count 'For Uy spring
            Case Else
                rowCountPerDepth = 1 * pyList.Item(depthloop).Count ' Both Ux and Uy
        End Select
        
        ' delegate the sub loop to a helper. update row count outside of the helper
        If printDirOne Then
        
            PrintSapHelper startRow, depthloop, namePreference, 1
            startRow = startRow + rowCountPerDepth
        End If
        
        If printDirTwo Then
        
            PrintSapHelper startRow, depthloop, namePreference, 2
            startRow = startRow + rowCountPerDepth
        End If
        
SkipCurrentLoopToPrintSAPSpring:
    
    Next depthloop
    

End Sub

' @Description("This helpe subroutine will print out one depth of PY link, given the starting position, DOF, modifiers, etc.")
Private Sub PrintSapHelper(startRow As Long, depthCount As Long, namePreference As Long, direction As Long)

    Dim modifier As Long
    Dim pyLoop As Long, linkNumber As Long
    Dim linkDirection As String, linkName As String
    Dim sapSetting(4) As String
    
    sapSetting(0) = "No"                         'If fixed
    sapSetting(1) = "Yes"                        'Is nonlinear
    sapSetting(2) = "0"                          'TransKE
    sapSetting(3) = "0"                          'TransCE

    With Worksheets("SAP Output")
    
        ' Find out what modifier it should use for the p data
        Select Case direction
        Case 0
            modifier = 1                         'Nothing to multiply. Just the raw data
        Case 1
            modifier = pyModifiersDirectionOne(depthCount) 'For Ux spring, grab the modifier at that depth
        Case 2
            modifier = pyModifiersDirectionTwo(depthCount) 'For Uy spring, grab the modifier at that depth
        Case Else
            modifier = 1                         'By default no input is selected (which shouldn't happen)
        End Select
        
        If namePreference = 0 Then
            linkNumber = depthCount + 1
        Else
            linkNumber = depthList.Item(depthCount)(1)
        End If
            
        If direction = 1 Then
            linkDirection = directionOne
        Else
            linkDirection = directionTwo
        End If
        
        ' Check name overrides
        If (userPreferenceName(depthCount) <> 0) Then
            linkName = userPreferenceName(depthCount)
        Else
            linkName = linkNameCustom & linkNameDivider & linkNumber
        End If
        
        
        ' Output actual PY Spring data
        ' Within each depth, loop through each entry of within that depth
        For pyLoop = 0 To pyList.Item(depthCount).Count - 1
        
            ' Generate non py data for sap to read
            .Cells(startRow + pyLoop, 1) = linkName 'Name
            .Cells(startRow + pyLoop, 2) = linkDirection 'DOF
            .Cells(startRow + 0, 3) = sapSetting(0) 'Fixed
            .Cells(startRow + 0, 4) = sapSetting(1) 'NonLinear
            .Cells(startRow + 0, 5) = sapSetting(2) 'TransKE
            .Cells(startRow + 0, 7) = sapSetting(3) 'TransCE
            
            .Cells(startRow + pyLoop, 10) = pyList.Item(depthCount)(pyLoop)(1) * modifier 'the p data
            .Cells(startRow + pyLoop, 11) = pyList.Item(depthCount)(pyLoop)(0) 'the y data
            
        
        Next pyLoop
    
    End With

End Sub

Private Sub DisplayMessage(msg As String)

    MsgBox msg

End Sub

Private Sub UpdateSystemStatus(msg As String)

    With Worksheets("Main Page")
    
        .Cells(10, 3) = msg
    
    End With
    
End Sub

' @Description("This subroutine read the input file and operates as a finite state machine")
Private Sub ReadDataFSM(printDepthOnly As Boolean)

' By utilizing (3) unique-ish keywords in the report:
'  | "Specified Depths for Output of p-y Curves"
'  | "p-y Curves Reported for Specified Depths"
'  | "Depth of p-y curve below pile head"
' This method identifies the number of depths, and the py Spring data within each depth.
' Then it extracts those table and output in excel.

' Data Structure:
' (2) Main arraylists that both in order of depth:
'       1. Arraylist of depth (Index: 0+, Item: depth)
'       2. Arraylist of pySpring (Index: 0+, Item: arrayList of pySpring at that depth
'           which can be retrieved from the first ArrayList)

' A visual example of the data structure is shown below:

' Index             depthList           pyList
' ------            ------              ------
' 0                 1                   {["0", "0"], ["0.035", "20.43"], ["0.08", "39.38"], ...}
' 1                 4                   {["0", "0"], ["0.032", "66.77"], ["0.08", "128.7"], ...}
' 2                 7                   {["0", "0"], ["0.031", "84.42"], ["0.08", "162.6"], ...}
' n                 depth @ n           {["y_n_1", "p_n_1"], ["y_n_2", "p_n_2"], ... , ["y_n_m", "p_n_m"]}
    
    
    ' Declare variables
    ' False = get everything
    ' True = get depth only
   
    Dim currentLine As String
    Dim WrdArray() As String
    Dim lineCount As Long
    Dim machineState As Long
    Dim pyDepth, pyDepthPrint As Boolean
    Dim pyTable, pyTablePrint As Boolean
    Dim tableCount As Long
    
    ' Late binding since ArrayList is not part of the standard VBA Library
    ' will encounter Automation Error if .NET Framework 3.5 is not installed
    Set depthList = CreateObject("System.Collections.ArrayList")
    Set pyList = CreateObject("System.Collections.ArrayList")
    Set pyListLocal = CreateObject("System.Collections.ArrayList")

    ' Initialize variables
    ' excel row/col index starts with 1
    lineCount = 0
    tableCount = 0
    machineState = 0
    pyDepth = False
   
    Close #1
    
    ' Begin Reading the txt output file
    Open fileName For Input As #1
   
    ' Begin looping through output file line by line until end of file (EOF)
    Do Until EOF(1)
        ' Read one line
        Line Input #1, currentLine
        ' Skip the line with space only
        If Not currentLine = "" Then
            ' Delete extra spaces
            currentLine = Application.WorksheetFunction.Trim(currentLine)
            
            machineState = FSMHelper(machineState, currentLine, tableCount)
            
            If machineState = 3 And printDepthOnly = True Then
                GoTo TerminateReadingFile       'Exit Sub
            End If
             
        End If
        
    ' continue line
    Loop
    
TerminateReadingFile:
    ' Once the txt file finished processing, close file
    Close #1
            


End Sub

' @Description("core code for the finite state machine")
Private Function FSMHelper(ByVal machineState As Long, ByVal inputLine As String, ByVal pyTableCount As Long) As Long

    ' For each state, read line by line
    ' State 0:  Idle State
    '           Transition to next state if it reads "Specified Depths for Output of p-y Curves"
    ' State 1:  Near depth list
    '           Transition to next state if it reads a numerial number
    ' State 2:  At depth list
    '           Transition to next state if it reads a non-numerical number
    ' State 3:  Idle State
    '           Transition to next state if it reads "p-y Curves Reported for Specified Depths"
    ' State 4:  Near py Table
    '           If reads "Depth of p-y curve below pile head" check depth
    '           Transition to state 5 if it reads numerical
    ' State 5:  At py table
    '           Populate PY table data
    '           Transitin to state 4 if it reads non-numerical
    '           Trantition to state 6 if it reads non-numerical and at last table
    ' State 6:  End of reading

    Select Case machineState
        Case 0: 'idle state
            If inputLine = "Specified Depths for Output of p-y Curves" Then
                ' kick it to the next state
                machineState = machineState + 1
            End If
            
        Case 1: 'Near depth list
            If IsNumeric(Left(inputLine, 1)) Then
                ' populate first entry of the table
                depthList.Add Split(inputLine, " ")

                machineState = machineState + 1
            End If
            
        Case 2: 'At depth list
            ' populate table
            If IsNumeric(Left(inputLine, 1)) Then
                ' populate entries of the table
                depthList.Add Split(inputLine, " ")
            Else
                machineState = machineState + 1
            End If
            
        Case 3: 'idle state
            If inputLine = "p-y Curves Reported for Specified Depths" Then
                machineState = machineState + 1
            End If
            
        Case 4: 'Near py table
            ' Check current depth table
            If Left(inputLine, 34) = "Depth of p-y curve below pile head" Then
                ' do something
            End If
            
            ' check if the table is here
            If IsNumeric(Left(inputLine, 1)) Then
                ' populate first entry of the table
                pyListLocal.Add Split(inputLine, " ")
                
                machineState = machineState + 1
            End If
            
        Case 5: 'At py table
            ' populate table
            If IsNumeric(Left(inputLine, 1)) Then
                ' populate the table at that depth
                pyListLocal.Add Split(inputLine, " ")
            Else
                ' recompile tables
                Dim pyArray(2) As String
                Dim pyListLocalClone As Object
                Dim localLoop As Long
                Set pyListLocalClone = pyListLocal.Clone
                
                ' with a cloned array, inverse and insert to front
                For localLoop = 1 To pyListLocal.Count - 1
                    pyArray(0) = pyListLocalClone(localLoop)(0) * -1
                    pyArray(1) = pyListLocalClone(localLoop)(1) * -1
                    pyListLocal.Insert 0, pyArray
                Next localLoop
                
                ' Transfer all local pyList value to global pyList
                pyList.Add pyListLocal.Clone
                ' Clear current arrayList for the next round of extraction
                pyListLocal.Clear
                
                If pyList.Count = depthList.Count Then
                    machineState = machineState + 1
                Else
                    machineState = 4
                End If
            End If
            
        Case Else:
            ' nothing
    End Select
    
    ' mark return value
    FSMHelper = machineState

End Function